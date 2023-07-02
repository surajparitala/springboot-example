package com.suraj.springbootexample.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
@RequiredArgsConstructor
@Slf4j
public class CustomerJDBCDataAccessService implements CustomerDAO{
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;
    @Override
    public List<Customer> getAllCustomers() {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ?, ?)
                """;

        int update = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());

        log.info("jdbcTemplate.update {}", update);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT COUNT(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerById(Long id) {
        var sql = """
                SELECT COUNT(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                DELETE
                FROM customer
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getAge() != null) {
            var sql = """
                    UPDATE customer SET age = ?
                    WHERE id = ?
                    """;
            jdbcTemplate.update(sql, customer.getAge(), customer.getId());
        }
        if (customer.getEmail() != null) {
            var sql = """
                    UPDATE customer SET email = ?
                    WHERE id = ?
                    """;
            jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
        }
        if (customer.getName() != null) {
            var sql = """
                    UPDATE customer SET name = ?
                    WHERE id = ?
                    """;
            jdbcTemplate.update(sql, customer.getName(), customer.getId());
        }
    }
}
