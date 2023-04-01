package client;

import UserModel.User;
import UserModel.UserCredentials;
import client.base.Client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String USER = "auth/";
    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER +  "register/")
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(USER + "email/")
                .then();
    }

    @Step("Обновление данных пользователя")
    public ValidatableResponse updateUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(USER + "user/")
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
/*        if (accessToken == null) {
            return;
        }*/
        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .when()
                .delete(USER + "user/")
                .then()
/*                .assertThat()
                .statusCode(202)
                .extract()
                .path("ok")*/;
    }
}
