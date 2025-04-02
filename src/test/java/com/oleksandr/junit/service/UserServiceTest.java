package com.oleksandr.junit.service;

import com.oleksandr.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) one instance of UserServiceTest for every methods
@TestInstance(TestInstance.Lifecycle.PER_METHOD) // default, one instance for one method

public class UserServiceTest {

    private UserService userService;

    private static final User IVAN = User.of(1,"Ivan","123");
    private static final User OLEK = User.of(2,"Olek","111");

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
        userService.add(IVAN);
        userService.add(OLEK);
        var users = userService.getAll();

        assertEquals(2, users.size());
    }
    @Test
    void successLoginIfUserExists(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());
        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals(user, IVAN));
    }

    @Test
    void loginFailedIfPasswordIsNotCorrect(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword()+"123");
        assertTrue(maybeUser.isEmpty());
    }
    @Test
    void loginFailedIfUserDoesNotExist(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername()+"123", IVAN.getPassword());
        assertTrue(maybeUser.isEmpty());
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
