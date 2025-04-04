package com.oleksandr.junit.service;

import com.oleksandr.junit.dto.User;
import com.oleksandr.junit.paramresolver.UserServiceParamResolver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) one instance of UserServiceTest for every methods
@TestInstance(TestInstance.Lifecycle.PER_METHOD) // default, one instance for one method

@Tag("fast")
@Tag("user")

//@TestMethodOrder(MethodOrderer.Random.class) //random order for tests
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// if we use @Order(INT) Annotation to methods to make an order
//@TestMethodOrder(MethodOrderer.MethodName.class) //order by alphabetic names of methods

//WARNING: CHAIN ANTIPATTERN!!!

// we can use for naming, but.. tests should have names with information by default xD
//@TestMethodOrder(MethodOrderer.DisplayName.class) used in pair with annotation
// @DisplayName("STRING") to write a name for tests

//we can use maven to run with tags:
// mvn clean test -Dgroups=login  -> include
// mvc clean test -DexcludedGroups=login -> exclude

@ExtendWith({
        UserServiceParamResolver.class
})
public class UserServiceTest {

    private UserService userService;

    public UserServiceTest(TestInfo testInfo) {
        System.out.println("contractor");
    }

    private static final User IVAN = User.of(1,"Ivan","123");
    private static final User OLEK = User.of(2,"Olek","111");

    @BeforeAll
    static void init(){
        System.out.println("Before ALL: ");
    }


    @BeforeEach
    void prepare(UserService service){
        System.out.println("Before each: " + this);
        userService = service;
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


    @Nested
    class LoginTest{
        @RepeatedTest(3)
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



        //    @ArgumentsSource(UserServiceParamResolver.class)

        //    @EmptySource
        //    @NullSource  works with 1  param   variants
        //    @NullAndEmptySource

        //    @ValueSource(
        //            strings = {"Ivan", "Petr"}
        //    )
        @ParameterizedTest
        @MethodSource("com.oleksandr.junit.service.UserServiceTest#getArgumentsForLoginTest")
        void loginParametrizedTestWithMethodSource(String username, String password, Optional<User> user){
            userService.add(IVAN);
            userService.add(OLEK);
            var maybeUser = userService.login(username, password);
            assertThat(maybeUser).isEqualTo(user);
        }
        @ParameterizedTest
        @CsvFileSource(resources = "/our-users.csv", delimiter = ',', numLinesToSkip = 1)
        @CsvSource({
                "Ivan,123",
                "Olek,111"
        })
        void loginParametrizedTestWithCSVSource(String username, String password){
            userService.add(IVAN);
            userService.add(OLEK);
            var maybeUser = userService.login(username, password);
            assertThat(maybeUser).isNotNull();
        }
    }
    static Stream<Arguments> getArgumentsForLoginTest(){
        return Stream.of(

                Arguments.of(IVAN.getUsername(), IVAN.getPassword(), Optional.of(IVAN)),
                Arguments.of(OLEK.getUsername(), OLEK.getPassword(), Optional.of(OLEK)),
                Arguments.of(OLEK.getUsername()+"123", OLEK.getPassword(), Optional.empty()),
                Arguments.of(OLEK.getUsername(), OLEK.getPassword()+"123", Optional.empty())
        );
    }

}
