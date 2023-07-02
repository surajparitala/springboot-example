package com.suraj.springbootexample.customer;

import org.apache.el.parser.AstPlus;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Long id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existsCustomerById(Long id);
    void deleteCustomerById(Long id);
    void updateCustomer(Customer customer);
}
