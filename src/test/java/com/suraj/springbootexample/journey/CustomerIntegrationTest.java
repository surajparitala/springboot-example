package com.suraj.springbootexample.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.suraj.springbootexample.customer.Customer;
import com.suraj.springbootexample.customer.CustomerRegistrationRequest;
import com.suraj.springbootexample.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {
    public static final String API_V_1_CUSTOMERS = "api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    @Test
    void canRegisterACustomer() {
        //Create Registration Request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email =  fakerName.lastName()+ UUID.randomUUID()+"@suraj123.com";
        int age  = RANDOM.nextInt();
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name, email, age
        );
        //Send a POST request
        webTestClient.post()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //Get all customers
        List<Customer> customerList = webTestClient.get()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(
                name, email, age
        );

        //Make sure that customer is present
        assertThat(customerList)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);
        //Get Customer By ID
        assert customerList != null;
        long id= customerList.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);

        webTestClient.get()
                .uri(API_V_1_CUSTOMERS+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        //Create Registration Request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email =  fakerName.lastName()+ UUID.randomUUID()+"@suraj123.com";
        int age  = RANDOM.nextInt();
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name, email, age
        );
        //Send a POST request
        webTestClient.post()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //Get all customers
        List<Customer> customerList = webTestClient.get()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //Get Customer By ID
        assert customerList != null;
        long id= customerList.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        webTestClient.delete()
                .uri(API_V_1_CUSTOMERS+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(API_V_1_CUSTOMERS+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        //Create Registration Request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email =  fakerName.lastName()+ UUID.randomUUID()+"@suraj123.com";
        int age  = RANDOM.nextInt();
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name, email, age
        );
        //Send a POST request
        webTestClient.post()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //Get all customers
        List<Customer> customerList = webTestClient.get()
                .uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        CustomerUpdateRequest updatableCustomer = new CustomerUpdateRequest(
                fakerName.fullName()+"new", email, RANDOM.nextInt()
        );

        //Update Customer
        assert customerList != null;
        long id= customerList.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        webTestClient.put()
                .uri(API_V_1_CUSTOMERS+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatableCustomer), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Get Customer By ID

        Customer updatedCustomer = webTestClient.get()
                .uri(API_V_1_CUSTOMERS + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedCustomer).satisfies(customer -> {
            assertThat(customer.getAge()).isEqualTo(updatableCustomer.age());
            assertThat(customer.getEmail()).isEqualTo(updatableCustomer.email());
            assertThat(customer.getName()).isEqualTo(updatableCustomer.name());
        });
    }
}
