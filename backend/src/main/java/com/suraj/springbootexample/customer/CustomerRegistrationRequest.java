package com.suraj.springbootexample.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age) {
}
