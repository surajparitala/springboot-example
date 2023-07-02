package com.suraj.springbootexample.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {
    public static final String MAIL = "xyz@gmail.com";
    private CustomerJPADataAccessService customerJPADataAccessService;
    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        customerJPADataAccessService = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllCustomers() {
        //When
        customerJPADataAccessService.getAllCustomers();
        //Then
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        //Given
        long id = 1;
        //When
        customerJPADataAccessService.getCustomerById(id);
        //Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        Customer customer = mock(Customer.class);
        //When
        customerJPADataAccessService.insertCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //When
        customerJPADataAccessService.existsPersonWithEmail(MAIL);
        //Then
        verify(customerRepository).existsCustomerByEmail(MAIL);
    }

    @Test
    void existsCustomerById() {
        //Given
        long id = 1;
        //When
        customerJPADataAccessService.existsCustomerById(id);
        //Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        //Given
        long id = 1;
        //When
        customerJPADataAccessService.deleteCustomerById(id);
        //Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer customer = mock(Customer.class);
        //When
        customerJPADataAccessService.updateCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }
}