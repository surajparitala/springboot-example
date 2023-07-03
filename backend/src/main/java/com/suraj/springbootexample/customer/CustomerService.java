package com.suraj.springbootexample.customer;

import com.suraj.springbootexample.exception.DuplicateResourceException;
import com.suraj.springbootexample.exception.RequestValidationException;
import com.suraj.springbootexample.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public Customer getCustomerById(Long id) {
        return customerDAO.getCustomerById(id).orElseThrow(() -> new ResourceNotFound("Customer Not Found with ID: %s".formatted(id)));
    }

    public void saveCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDAO.existsPersonWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Customer with email exists");
        }

        Customer customer = new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(), customerRegistrationRequest.age());
        customerDAO.insertCustomer(customer);
    }

    public void deleteCustomer(Long id) {
        if (!customerDAO.existsCustomerById(id)) {
            throw new ResourceNotFound("Customer With ID Cannot be deleted since not found");
        }
        customerDAO.deleteCustomerById(id);
    }

    public void updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = getCustomerById(id);
        boolean changes = false;
        if (request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            changes = true;
        }

        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            changes = true;
        }

        if (request.email() != null && !request.email().equals(customer.getEmail())) {
            if (customerDAO.existsPersonWithEmail(request.email())) {
                throw  new DuplicateResourceException("Email Already Taken");
            }
            customer.setEmail(request.email());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No Data Changes Found");
        }
        customerDAO.updateCustomer(customer);
    }
}
