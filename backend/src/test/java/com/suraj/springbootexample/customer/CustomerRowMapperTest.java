package com.suraj.springbootexample.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {
    private CustomerRowMapper customerRowMapper;

    @BeforeEach
    void setUp() {
        customerRowMapper = new CustomerRowMapper();
    }

    @Test
    void mapRow() throws SQLException {
        //Given
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("Suraj");
        when(resultSet.getString("email")).thenReturn("raj@gmail.com");
        when(resultSet.getInt("age")).thenReturn(19);
        //When
        Customer customer = customerRowMapper.mapRow(resultSet, 1);
        //Then
        assertThat(customer).isNotNull();
        assertThat(customer.getAge()).isEqualTo(19);
        assertThat(customer.getName()).isEqualTo("Suraj");
        assertThat(customer.getId()).isEqualTo(1L);
        assertThat(customer.getEmail()).isEqualTo("raj@gmail.com");
    }
}