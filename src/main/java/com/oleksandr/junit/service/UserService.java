package com.oleksandr.junit.service;

import com.oleksandr.junit.dto.User;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> getAll(){
        return users;
    }

    public void add(User user) {
        users.add(user);
    }

    public Optional<User> login(String username, String password) {

        if(username == null || password == null) {
            throw new IllegalArgumentException("Username or Password is null");
        }
        return users.stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst();
    }

    public void addAll(User...users) {
        this.users.addAll(Arrays.asList(users));
    }

    public Map<Integer, User> getAllConvertedById() {
        return users.stream().collect(toMap(User::getId, Function.identity()));
    }
}
