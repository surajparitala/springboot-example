package com.suraj.springbootexample.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{
    private static final List<Customer> customerList;
    static {
        customerList = new ArrayList<>();
        Customer alex = new Customer(1L, "Alex", "alex@gmail.com", 21);
        Customer jamila = new Customer(1L, "Jamila", "jamila@gmail.com", 21);
        customerList.add(alex);
        customerList.add(jamila);
    }
    @Override
    public List<Customer> getAllCustomers() {
        return customerList;
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerList.stream().filter(customer -> customer.getId().equals(id)).findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerList.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerList.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerById(Long id) {
        return customerList.stream().anyMatch(customer -> customer.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerList.remove(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerList.add(customer);
    }
}
