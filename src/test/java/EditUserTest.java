import io.qameta.allure.restassured.AllureRestAssured;
import usermodel.User;
import usermodel.UserGenerator;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class EditUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken, email, name, message;
    private int statusCode;
    boolean isSuccess;
    private ValidatableResponse UpdateUserResponse;
    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
    }
    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userClient = new UserClient();
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
    }
    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changingUserDataWithAuthorizationTest() {
        User updateUser = UserGenerator.getUser();
        ValidatableResponse UpdateUserResponse = userClient.updateUser(updateUser, accessToken);
        statusCode = UpdateUserResponse.extract().statusCode();
        isSuccess = UpdateUserResponse.extract().path("success");
        email = UpdateUserResponse.extract().path("user.email");
        name = UpdateUserResponse.extract().path("user.name");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertEquals(updateUser.getEmail(), email);
        assertEquals(updateUser.getName(), name);
    }
    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    public void changeUserEmailWithAuthorizationTest() {
        User updatEmailUser = new User(UserGenerator.getUser().getEmail(), user.getPassword(), user.getName());
        UpdateUserResponse = userClient.updateUser(updatEmailUser, accessToken);
        statusCode = UpdateUserResponse.extract().statusCode();
        isSuccess = UpdateUserResponse.extract().path("success");
        email = UpdateUserResponse.extract().path("user.email");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertEquals(updatEmailUser.getEmail(), email);
    }
    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    public void changeUserPasswordWithAuthorizationTest() {
        User updatePasswordUser = new User(user.getEmail(), UserGenerator.getUser().getPassword(), user.getName());
        UpdateUserResponse = userClient.updateUser(updatePasswordUser, accessToken);
        statusCode = UpdateUserResponse.extract().statusCode();
        isSuccess = UpdateUserResponse.extract().path("success");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
    }
    @Test
    @DisplayName("Изменение email пользователя на email другого пользователя(существующего) с авторизацией")
    public void changeUserEmailToAnotherUserEmailWithAuthorization() {
        User updateEmailExUser = new User(user.getEmail(), UserGenerator.getUser().getPassword(), UserGenerator.getUser().getName());
        ValidatableResponse doubleReg = userClient.createUser(updateEmailExUser);
        statusCode = doubleReg.extract().statusCode();
        isSuccess = doubleReg.extract().path("success");
        message = doubleReg.extract().path("message");

        assertEquals(SC_FORBIDDEN, statusCode);
        assertFalse(isSuccess);
        assertEquals("User already exists", message);
    }
    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changingUserDataWithoutAuthorizationTest() {
        User updateUser = UserGenerator.getUser();
        UpdateUserResponse = userClient.updateUser(updateUser, "");
        statusCode = UpdateUserResponse.extract().statusCode();
        message = UpdateUserResponse.extract().path("message");

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals("You should be authorised", message);
    }
}
