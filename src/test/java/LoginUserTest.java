import usermodel.User;
import usermodel.UserCredentials;
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
import static org.junit.Assert.assertNotNull;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private ValidatableResponse response;
    private ValidatableResponse loginResponse;
    private int statusCode;
    boolean isSuccess;
    private String message;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter()
        );
    }
    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userClient = new UserClient();
        response = userClient.createUser(user);
    }
    @After
    public void tearDown() {
        accessToken = response.extract().path("accessToken");
        userClient.deleteUser(accessToken);
    }
    @Test
    @DisplayName("Проверка авторизации с существующим логином и паролем")
    public void authorizationWithAnExistingUsernameAndPasswordTest() {
        loginResponse = userClient.loginUser(UserCredentials.from(user));
        statusCode = loginResponse.extract().statusCode();
        isSuccess = loginResponse.extract().path("success");
        accessToken = loginResponse.extract().path("accessToken");
        String refreshToken = loginResponse.extract().path("refreshToken");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
    }
    @Test
    @DisplayName("Проверка авторизация без ввода email")
    public void authorizationWithoutEmailTest() {
        user.setEmail("");
        loginResponse = userClient.loginUser(UserCredentials.from(user));
        isSuccess = loginResponse.extract().path("success");
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isSuccess);
        assertEquals("email or password are incorrect", message);
    }
    @Test
    @DisplayName("Проверка авторизация без ввода пароля")
    public void authorizationWithoutPasswordTest() {
        user.setPassword("");
        loginResponse = userClient.loginUser(UserCredentials.from(user));
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isSuccess);
        assertEquals("email or password are incorrect", message);
    }
    @Test
    @DisplayName("Проверка авторизация без ввода пароля и логина")
    public void authorizationWithoutEmailAndPasswordTest() {
        user.setEmail("");
        user.setPassword("");
        loginResponse = userClient.loginUser(UserCredentials.from(user));
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isSuccess);
        assertEquals("email or password are incorrect", message);
    }
}
