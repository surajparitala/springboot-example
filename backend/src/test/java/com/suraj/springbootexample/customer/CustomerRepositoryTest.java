package com.suraj.springbootexample.customer;

import com.suraj.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        customerRepository.save(customer);
        //When
        var actual = customerRepository.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailWhenNotPresent() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        //When
        var actual = customerRepository.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        customerRepository.save(customer);
        Long id = customerRepository.findAll().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //When
        var actual = customerRepository.existsCustomerById(id);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdWhenRandomIdIsPassed() {
        //Given
        long id = -1;
        //When
        var actual = customerRepository.existsCustomerById(id);
        //Then
        assertThat(actual).isFalse();
    }
}