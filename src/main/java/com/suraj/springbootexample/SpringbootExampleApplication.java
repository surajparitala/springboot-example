package com.suraj.springbootexample;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.suraj.springbootexample.customer.Customer;
import com.suraj.springbootexample.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class SpringbootExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootExampleApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {

        return args -> {
            var faker = new Faker();
            Random random = new Random();
            var name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            Customer alex = new Customer(firstName+" "+lastName, firstName.toLowerCase() +"."+ lastName.toLowerCase()+"@example.com", random.nextInt(16, 99));
            customerRepository.save(alex);
        };
    }
}
