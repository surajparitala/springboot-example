package com.suraj.springbootexample.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age) {
}
