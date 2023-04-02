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

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class CreateUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private ValidatableResponse response;
    private int statusCode;
    boolean isSuccess;

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
        response = userClient.createUser(user);
    }
    @After
    public void tearDown() {
        accessToken = response.extract().path("accessToken");
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    public void userSuccessCreateTest() {
        statusCode = response.extract().statusCode();
        isSuccess = response.extract().path("success");
        accessToken = response.extract().path("accessToken");
        String refreshToken = response.extract().path("refreshToken");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
    }

    @Test
    @DisplayName("Cоздать пользователя, который уже зарегистрирован")
    public void createAnExistingUserTest() {
        ValidatableResponse secondResponse = userClient.createUser(user);
        String message = secondResponse.extract().path("message");
        statusCode = secondResponse.extract().statusCode();
        isSuccess = secondResponse.extract().path("success");

        assertFalse(isSuccess);
        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals("User already exists", message);
    }

    @Test
    @DisplayName("Cоздать пользователя и не заполнить одно из обязательных полей - email")
    public void createUserWithoutLoginTest() {
        User getUserWithoutLogin = UserGenerator.getUserWithoutEmail();
        ValidatableResponse wrongResponse = userClient.createUser(getUserWithoutLogin);
        String message = wrongResponse.extract().path("message");
        statusCode = wrongResponse.extract().statusCode();
        isSuccess = wrongResponse.extract().path("success");

        assertFalse(isSuccess);
        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals("Email, password and name are required fields", message);
    }

    @Test
    @DisplayName("Cоздать пользователя и не заполнить одно из обязательных полей - password")
    public void createUserWithoutPasswordTest() {
        User getUserWithoutPassword = UserGenerator.getUserWithoutPassword();
        ValidatableResponse wrongResponse = userClient.createUser(getUserWithoutPassword);
        String message = wrongResponse.extract().path("message");
        statusCode = wrongResponse.extract().statusCode();
        isSuccess = wrongResponse.extract().path("success");

        assertFalse(isSuccess);
        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals("Email, password and name are required fields", message);
    }

    @Test
    @DisplayName("Cоздать пользователя и не заполнить одно из обязательных полей - name")
    public void createUserWithoutNameTest() {
        User getUserWithoutNameName = UserGenerator.getUserWithoutName();
        ValidatableResponse wrongResponse = userClient.createUser(getUserWithoutNameName);
        String message = wrongResponse.extract().path("message");
        statusCode = wrongResponse.extract().statusCode();
        isSuccess = wrongResponse.extract().path("success");

        assertFalse(isSuccess);
        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals("Email, password and name are required fields", message);
    }
}
