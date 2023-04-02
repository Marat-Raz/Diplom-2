import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import ordermodel.OrderGeneration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import usermodel.User;
import usermodel.UserGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrderTest {
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
    @DisplayName("Получение списка заказов авторизованного пользователя с пустым списком")
    public void getOrderWithAuthTest() {
        response = orderClient.getUserOrder(accessToken);
        statusCode = response.extract().statusCode();
        isSuccess = response.extract().path("success");
        int total = response.extract().path("total");
        int totalToday = response.extract().path("totalToday");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertNotNull(total);
        assertNotNull(totalToday);
    }
    @Test
    @DisplayName("Получение списка заказов неавторизованного пользователя")
    public void getOrderWithNonAuthTest() {
        response = orderClient.getUserOrder("");
        statusCode = response.extract().statusCode();
        isSuccess = response.extract().path("success");
        message = response.extract().path("message");

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isSuccess);
        assertEquals("You should be authorised",message);
    }
    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя с непустым списком")
    public void getOrderWithAuthWithNonEmptyListTest() {
        response = orderClient.createOrder(ingredients, accessToken);
        response = orderClient.getUserOrder(accessToken);
        statusCode = response.extract().statusCode();
        isSuccess = response.extract().path("success");
        ArrayList<String> ordersIngredients = response.extract().path("orders[0].ingredients");
        String orders_id = response.extract().path("orders[0]._id");
        String ordersStatus = response.extract().path("orders[0].status");
        int ordersNumber = response.extract().path("orders[0].number");
        String ordersCreatedAt = response.extract().path("orders[0].createdAt");
        String ordersUpdatedAt = response.extract().path("orders[0].updatedAt");
        int total = response.extract().path("total");
        int totalToday = response.extract().path("totalToday");

        assertEquals(SC_OK, statusCode);
        assertTrue(isSuccess);
        assertNotNull(ordersIngredients);
        assertNotNull(orders_id);
        assertNotNull(ordersStatus);
        assertNotNull(ordersNumber);
        assertNotNull(ordersCreatedAt);
        assertNotNull(ordersUpdatedAt);
        assertNotNull(total);
        assertNotNull(totalToday);
    }
}
