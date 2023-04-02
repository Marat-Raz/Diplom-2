package client;

import client.base.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ordermodel.Order;
import ordermodel.OrderGeneration;
import ordermodel.OrderNumber;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String ORDER = "orders/";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(OrderGeneration ingredients, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(ingredients)
                .when()
                .post(ORDER)
                .then();
    }
    @Step("Получить заказы конкретного пользователя")
    public ValidatableResponse getUserOrder(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDER)
                .then();
    }

}