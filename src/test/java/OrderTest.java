import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import ordermodel.Order;
import ordermodel.OrderGeneration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import usermodel.User;
import usermodel.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderTest {

    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private String accessToken, message, userName, userEmail;
    private ValidatableResponse response;
    boolean isSuccess;
    private int statusCode, orderNumber;
    private OrderGeneration ingredients, nonIngredients;
    private User user;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
    }
    @Before
    public void SetUp() {
        ingredients = OrderGeneration.getIngredients();
        nonIngredients = OrderGeneration.getEmptyIngredients();
        user = UserGenerator.getUser();
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
    }
    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }
    @Test
    @DisplayName("Создание заказа с ингредиентами, без авторизации")
    public void createOrderWithoutAuthTest() {
        response = orderClient.createOrder(ingredients, "");
        statusCode = response.extract().statusCode();
        isSuccess = response.extract().path("success");
        orderNumber = response.extract().path("order.number");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertNotNull(orderNumber);
    }
    @Test
    @DisplayName("Создание заказа с ингредиентами, с авторизацией")
    public void createOrderWithAuthTest() {
        response = orderClient.createOrder(ingredients, accessToken);
        isSuccess = response.extract().path("success");
        statusCode = response.extract().statusCode();
        userName = response.extract().path("order.owner.name");
        userEmail = response.extract().path("order.owner.email");
        orderNumber = response.extract().path("order.number");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertEquals(user.getName(), userName);
        assertEquals(user.getEmail(), userEmail);
        assertNotNull(orderNumber);
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов, без авторизации")
    public void createOrderWithoutIngredientsTest() {
        response = orderClient.createOrder(nonIngredients, "");
        statusCode = response.extract().statusCode();
        isSuccess = response.extract().path("success");
        message = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertFalse(isSuccess);
        assertEquals("Ingredient ids must be provided", message);
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов, с авторизацией")
    public void createOrderWithoutIngredientsWithAuthTest() {
        response = orderClient.createOrder(nonIngredients, accessToken);
        statusCode = response.extract().statusCode();
        isSuccess = response.extract().path("success");
        message = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertFalse(isSuccess);
        assertEquals("Ingredient ids must be provided", message);
    }
    @Test
    @DisplayName("Создание заказа с неверным хешем")
    public void createOrderWithWrongHashIngredientsTest() {
        OrderGeneration wrongIngredients = OrderGeneration.getIncorrectHashOfIngredients();
        response = orderClient.createOrder(wrongIngredients, "");
        statusCode = response.extract().statusCode();

        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }
}
