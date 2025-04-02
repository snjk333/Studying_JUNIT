package com.oleksandr.junit.service;

import com.oleksandr.junit.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void prepare(){
        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Test
    void usersEmptyIfNoUserAdded() {
        System.out.println("Test1: " + this);
        var users = userService.getAll();
        assertTrue(users.isEmpty());
    }
    @Test
    void userSizeIfUserAdded() {
        
        System.out.println("Test2: " + this);
        userService.add(new User());
        userService.add(new User());
        var users = userService.getAll();

        assertEquals(2, users.size());
    }

}
