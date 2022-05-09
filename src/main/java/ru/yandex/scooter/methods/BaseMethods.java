package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import ru.yandex.scooter.models.BasePost;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class BaseMethods {

    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    @Step("Send Post request")
    public static Response sendPostRequest(BasePost request, String url) {
        Response response = given()
                .spec(getBaseSpec())
                .and()
                .body(request)
                .when()
                .post(url);
        return response;
    }

    @Step("Send Get request")
    public static Response sendGetRequest(String param, String url) {
        Response response = given()
                .spec(getBaseSpec())
                .and()
                .param(param)
                .get(url);
        return response;
    }

    @Step("Send Delete request")
    public static Response sendDeleteRequest(String request, String url) {
        Response response = given()
                .spec(getBaseSpec())
                .and()
                .body(request)
                .when()
                .delete(url);
        return response;
    }


    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        int responseCode = response.statusCode();
        Assert.assertEquals(code, responseCode);
    }

    @Step("Check body from response")
    public void checkBodyFromResponse(Response response, String expectedBody) {
        String responseBody = response.body().asString();
        Assert.assertEquals(expectedBody, responseBody);
    }

    @Step("Check body from response")
    public void checkBodyFromResponseWithNotNullValueCondition(Response response, String key) {
        response.then().assertThat().body(key, notNullValue());
    }

}
