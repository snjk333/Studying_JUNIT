package com.oleksandr.junit.service;

import com.oleksandr.junit.dto.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> getAll(){
        return users;
    }

    public void add(User user) {
        users.add(user);
    }

    public Optional<User> login(String username, String password) {
        return users.stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst();
    }
}
