package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.scooter.models.PostCourierModel;

import static io.restassured.RestAssured.given;


public class PostLoginMethods extends BaseMethods {


    @Step("Send Post request to /api/v1/courier/login: Login courier")
    public Response sendPostRequestLogin(PostCourierModel postCourier) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(postCourier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }


    @Step("Prepare data for post request: /api/v1/courier/login")
    public PostCourierModel prepareDataForPostRequestLogin(String login, String password) {
        PostCourierModel postCourier = new PostCourierModel(login, password);
        return postCourier;
    }

    @Step("Create new random courier")
    public PostCourierModel createRandomCourierForTest() {
        PostCourierModel postCourier = new PostCourierModel(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(postCourier)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");
        response.statusCode();
        return postCourier;
    }

}