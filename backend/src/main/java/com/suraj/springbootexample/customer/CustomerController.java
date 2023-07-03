package com.suraj.springbootexample.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable("id") Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public void createCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.saveCustomer(customerRegistrationRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("{id}")
    public void updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerUpdateRequest customerUpdateRequest ) {
        customerService.updateCustomer(id, customerUpdateRequest);
    }

}
