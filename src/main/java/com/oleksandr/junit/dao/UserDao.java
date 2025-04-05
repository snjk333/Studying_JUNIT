package com.oleksandr.junit.dao;

import lombok.SneakyThrows;

import java.sql.DriverManager;
import java.sql.SQLException;

public class UserDao {

    @SneakyThrows
    public boolean delete(int id) {
        try(var connection = DriverManager.getConnection("url", "username", "password")) {
            return true;
        }
    }
}
