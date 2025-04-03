package com.oleksandr.junit.service;

import com.oleksandr.junit.dto.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) one instance of UserServiceTest for every methods
@TestInstance(TestInstance.Lifecycle.PER_METHOD) // default, one instance for one method
@Tag("fast")
@Tag("user")

//we can use maven to run with tags:
// mvn clean test -Dgroups=login  -> include
// mvc clean test -DexcludedGroups=login -> exclude
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
//        assertTrue(users.isEmpty());
        assertThat(users).isEmpty();
    }
    @Test
    void userSizeIfUserAdded() {

        System.out.println("Test2: " + this);
        userService.add(IVAN);
        userService.add(OLEK);
        var users = userService.getAll();

//        assertEquals(2, users.size());
        assertThat(users).hasSize(2);
    }
    @Test
    void addAllUsers(){
        userService.addAll(IVAN,OLEK);
        var users = userService.getAll();
        assertThat(users).hasSize(2);
    }
    @Test
    @Tag("login")
    void successLoginIfUserExists(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

//        assertTrue(maybeUser.isPresent());
        assertThat(maybeUser).isPresent();

        //maybeUser.ifPresent(user -> assertEquals(user, IVAN));
        maybeUser.ifPresent(user -> assertThat(maybeUser).isEqualTo(Optional.of(IVAN)));
        //or
        assertThat(maybeUser).isEqualTo(Optional.of(IVAN));
    }

    @Test
    @Tag("login")
    void loginFailedIfPasswordIsNotCorrect(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword()+"123");
//        assertTrue(maybeUser.isEmpty());
        assertThat(maybeUser).isEmpty();
    }
    @Test
    @Tag("login")
    void loginFailedIfUserDoesNotExist(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername()+"123", IVAN.getPassword());
//        assertTrue(maybeUser.isEmpty());
        assertThat(maybeUser).isEmpty();
    }

    @Test
    @Tag("login")
    void throwExceptionIfUsernameOrPasswordIsNull(){

       var exeption = assertThrows(IllegalArgumentException.class, () -> userService.login(null, "123"));
        assertThat(exeption.getMessage()).isEqualTo("Username or Password is null");
        assertThrows(IllegalArgumentException.class, () -> userService.login("user", null));

    }



    @Test
    void usersConvertedToMapById(){
        userService.addAll(IVAN,OLEK);
        Map<Integer, User> userMap = userService.getAllConvertedById();

        assertAll(
                () ->
                assertThat(userMap).containsKeys(IVAN.getId(), OLEK.getId()),
                () ->
                assertThat(userMap).containsValues(IVAN, OLEK)
        );

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
