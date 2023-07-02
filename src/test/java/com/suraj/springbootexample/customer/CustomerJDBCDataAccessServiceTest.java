package com.suraj.springbootexample.customer;

import com.suraj.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {
    private CustomerJDBCDataAccessService customerJDBCDataAccessService;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        customerJDBCDataAccessService = new CustomerJDBCDataAccessService(
                getJdbcTemplate(), customerRowMapper
        );
    }

    @Test
    void getAllCustomers() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID(),
                20
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        //When
        List<Customer> allCustomers = customerJDBCDataAccessService.getAllCustomers();

        //Then
        assertThat(allCustomers).isNotEmpty();
    }

    @Test
    void getCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //When
        Optional<Customer> optionalCustomer = customerJDBCDataAccessService.getCustomerById(id);
        //Then
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenGetCustomerById() {
        long id = -1;
        Optional<Customer> optionalCustomer = customerJDBCDataAccessService.getCustomerById(id);
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void insertCustomer() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //When
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        Optional<Customer> optionalCustomer = customerJDBCDataAccessService.getCustomerById(id);
        //Then
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        //When
        boolean existsPersonWithEmail = customerJDBCDataAccessService.existsPersonWithEmail(email);
        //Then
        assertThat(existsPersonWithEmail).isTrue();
    }

    @Test
    void existsPersonWithEmailWillReturnFalseWhenNoEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        //When
        boolean existsPersonWithEmail = customerJDBCDataAccessService.existsPersonWithEmail(email);
        //Then
        assertThat(existsPersonWithEmail).isFalse();
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
        customerJDBCDataAccessService.insertCustomer(customer);
        //When
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        boolean existsCustomerById = customerJDBCDataAccessService.existsCustomerById(id);
        //Then
        assertThat(existsCustomerById).isTrue();
    }

    @Test
    void existsCustomerByIdWillReturnFalseWhenIdNotPresent() {
        //Given
        long id = -1;
        //When
        boolean existsCustomerById = customerJDBCDataAccessService.existsCustomerById(id);
        //Then
        assertThat(existsCustomerById).isFalse();
    }


    @Test
    void deleteCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //When
        customerJDBCDataAccessService.deleteCustomerById(id);
        Optional<Customer> optionalCustomer = customerJDBCDataAccessService.getCustomerById(id);

        //Then
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //When
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //Then
        Customer updateCustomer = new Customer();
        String newName = "Suraj";
        updateCustomer.setId(id);
        updateCustomer.setName(newName);
        customerJDBCDataAccessService.updateCustomer(updateCustomer);
        Optional<Customer> optionalCustomerPostUpdate = customerJDBCDataAccessService.getCustomerById(id);
        assertThat(optionalCustomerPostUpdate).isPresent().hasValueSatisfying(updatedCustomer -> {
            assertThat(updatedCustomer.getId()).isEqualTo(id);
            assertThat(updatedCustomer.getName()).isEqualTo(newName);
            assertThat(updatedCustomer.getEmail()).isEqualTo(email);
            assertThat(updatedCustomer.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //When
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //Then
        Customer updateCustomer = new Customer();
        int newAge = 22;
        updateCustomer.setId(id);
        updateCustomer.setAge(newAge);
        customerJDBCDataAccessService.updateCustomer(updateCustomer);
        Optional<Customer> optionalCustomerPostUpdate = customerJDBCDataAccessService.getCustomerById(id);
        assertThat(optionalCustomerPostUpdate).isPresent().hasValueSatisfying(updatedCustomer -> {
            assertThat(updatedCustomer.getId()).isEqualTo(id);
            assertThat(updatedCustomer.getName()).isEqualTo(customer.getName());
            assertThat(updatedCustomer.getEmail()).isEqualTo(email);
            assertThat(updatedCustomer.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void updateCustomerEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //When
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //Then
        Customer updateCustomer = new Customer();
        String newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        updateCustomer.setId(id);
        updateCustomer.setEmail(newEmail);
        customerJDBCDataAccessService.updateCustomer(updateCustomer);
        Optional<Customer> optionalCustomerPostUpdate = customerJDBCDataAccessService.getCustomerById(id);
        assertThat(optionalCustomerPostUpdate).isPresent().hasValueSatisfying(updatedCustomer -> {
            assertThat(updatedCustomer.getId()).isEqualTo(id);
            assertThat(updatedCustomer.getName()).isEqualTo(customer.getName());
            assertThat(updatedCustomer.getEmail()).isEqualTo(newEmail);
            assertThat(updatedCustomer.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAllTheValues() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //When
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //Then
        Customer updateCustomer = new Customer();
        String newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        updateCustomer.setId(id);
        updateCustomer.setEmail(newEmail);
        updateCustomer.setName("Suraj");
        updateCustomer.setAge(22);
        customerJDBCDataAccessService.updateCustomer(updateCustomer);
        Optional<Customer> optionalCustomerPostUpdate = customerJDBCDataAccessService.getCustomerById(id);
        assertThat(optionalCustomerPostUpdate).isPresent().hasValue(updateCustomer);
    }

    @Test
    void updateCustomerWithNothing() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //When
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService.getAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //Then
        Customer updateCustomer = new Customer();
        updateCustomer.setId(id);
        customerJDBCDataAccessService.updateCustomer(updateCustomer);
        Optional<Customer> optionalCustomerPostUpdate = customerJDBCDataAccessService.getCustomerById(id);
        assertThat(optionalCustomerPostUpdate).isPresent().hasValueSatisfying(updatedCustomer -> {
            assertThat(updatedCustomer.getId()).isEqualTo(id);
            assertThat(updatedCustomer.getName()).isEqualTo(customer.getName());
            assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
            assertThat(updatedCustomer.getAge()).isEqualTo(customer.getAge());
        });
    }
}