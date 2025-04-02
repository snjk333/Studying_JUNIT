package com.oleksandr.junit.service;

import com.oleksandr.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) one instance of UserServiceTest for every methods
@TestInstance(TestInstance.Lifecycle.PER_METHOD) // default, one instance for one method

public class UserServiceTest {

    private UserService userService;

    @BeforeAll
    static void init(){
        System.out.println("Before ALL: ");
    }


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
        userService.add(User.of(1,"Ivan","123"));
        userService.add(User.of(2,"Olek","111"));
        var users = userService.getAll();

        assertEquals(2, users.size());
    }


    @AfterEach
    void deleteDataFromDatabase(){
        System.out.println("After each: " + this + "\n\n");
    }

    @AfterAll
    static void end(){
        System.out.println("After ALL: ");
    }

}
