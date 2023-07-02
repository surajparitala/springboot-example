package com.suraj.springbootexample.customer;

import com.suraj.springbootexample.exception.DuplicateResourceException;
import com.suraj.springbootexample.exception.RequestValidationException;
import com.suraj.springbootexample.exception.ResourceNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService customerService;
    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerDAO);
    }

    @Test
    void getAllCustomers() {
        //When
        customerService.getAllCustomers();
        //Then
        verify(customerDAO).getAllCustomers();
    }

    @Test
    void getCustomerById() {
        //Given
        long id = 10;
        Customer customer = new Customer(id, "Alex", "Alex@gmail.com", 19);
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        Customer actual = customerService.getCustomerById(id);
        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getCustomerByIdFailWhenIdNotPresentIsPassed() {
        //Given
        long id = 10;
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> customerService.getCustomerById(id)).isInstanceOf(ResourceNotFound.class).hasMessage("Customer Not Found with ID: %s".formatted(id));
    }

    @Test
    void saveCustomer() {
        //Given
        String email = "Alex@gmail.com";
        CustomerRegistrationRequest customer = new CustomerRegistrationRequest("Alex", "Alex@gmail.com", 19);
        when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);
        //When
        customerService.saveCustomer(customer);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isEqualTo(null);
        assertThat(capturedCustomer.getName()).isEqualTo(customer.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.email());
    }

    @Test
    void saveCustomerTestWhenCustomerExists() {
        //Given
        String email = "Alex@gmail.com";
        CustomerRegistrationRequest customer = new CustomerRegistrationRequest("Alex", "Alex@gmail.com", 19);
        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);
        //When
        assertThatThrownBy(() -> customerService.saveCustomer(customer)).isInstanceOf(DuplicateResourceException.class).hasMessage("Customer with email exists");
        //Then

        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        //Given
        long id = 10;
        when(customerDAO.existsCustomerById(id)).thenReturn(true);
        //When
        customerService.deleteCustomer(id);
        //Then
        verify(customerDAO).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerThrowsExceptionWhenCustomerWithIDNotFound() {
        //Given
        long id = 10;
        when(customerDAO.existsCustomerById(id)).thenReturn(false);
        //When
        assertThatThrownBy(() -> customerService.deleteCustomer(id)).isInstanceOf(ResourceNotFound.class).hasMessage("Customer With ID Cannot be deleted since not found");
        //Then
        verify(customerDAO).existsCustomerById(id);
        verify(customerDAO, never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomerName() {
        //Given
        long id = 10;
        Customer customer = new Customer(id, "Alex", "Alex@gmail.com", 19);
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                "Alex John", null, null
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        customerService.updateCustomer(id, customerUpdateRequest);
        //Then
        verify(customerDAO).getCustomerById(id);
        verify(customerDAO).updateCustomer(customer);
    }

    @Test
    void updateCustomerEmail() {
        //Given
        long id = 10;
        Customer customer = new Customer(id, "Alex", "Alex@gmail.com", 19);
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                null, "AlexJohn@gmail.com", null
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        customerService.updateCustomer(id, customerUpdateRequest);
        //Then
        verify(customerDAO).getCustomerById(id);
        verify(customerDAO).updateCustomer(customer);
    }

    @Test
    void updateCustomerAge() {
        //Given
        long id = 10;
        Customer customer = new Customer(id, "Alex", "Alex@gmail.com", 19);
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                null, null, 29
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        customerService.updateCustomer(id, customerUpdateRequest);
        //Then
        verify(customerDAO).getCustomerById(id);
        verify(customerDAO).updateCustomer(customer);
    }

    @Test
    void updateCustomerAllFields() {
        //Given
        long id = 10;
        Customer customer = new Customer(id, "Alex", "Alex@gmail.com", 19);
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                "Alex John", "AlexJohn@gmail.com", 29
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        customerService.updateCustomer(id, customerUpdateRequest);
        //Then
        verify(customerDAO).getCustomerById(id);
        verify(customerDAO).updateCustomer(customer);
    }

    @Test
    void updateCustomerExceptionWhenCustomerNotFoundWihGivenID() {
        //Given
        long id = 10;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                "Alex John", "AlexJohn@gmail.com", 29
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> customerService.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(ResourceNotFound.class)
                        .hasMessage("Customer Not Found with ID: %s".formatted(id));
    }

    @Test
    void updateCustomerExceptionWhenNoUpdatableChanges() {
        //Given
        long id = 10;
        Customer customer = new Customer(id, "Alex", "Alex@gmail.com", 19);
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                null, null, null
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        //Then
        assertThatThrownBy(() -> customerService.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No Data Changes Found");
    }

    @Test
    void updateCustomerExceptionWhenUpdatingEmailAlreadyExists() {
        //Given
        long id = 10;
        String email = "Alex@gmail.com";
        Customer customer = new Customer(id, "Alex", "Alex123@gmail.com", 19);
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                null, email, null
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);
        //When
        //Then
        assertThatThrownBy(() -> customerService.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email Already Taken");
    }
}