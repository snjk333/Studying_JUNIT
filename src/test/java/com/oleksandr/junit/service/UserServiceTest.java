package com.oleksandr.junit.service;

import com.oleksandr.junit.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {


    @BeforeEach
    void prepare(){
        System.out.println("Before each: " + this);
    }

    @Test
    void usersEmptyIfNoUserAdded() {
        System.out.println("Test1: " + this);

        var userService = new UserService();
        var users = userService.getAll();
//        assertFalse(users.isEmpty(), () -> "Users list is empty"); we can add message
        assertTrue(users.isEmpty());
    }
    @Test
    void userSizeIfUserAdded() {
        System.out.println("Test2: " + this);

        var userService = new UserService();
        userService.add(new User());
        userService.add(new User());

        var users = userService.getAll();

        assertEquals(2, users.size());
    }

}
