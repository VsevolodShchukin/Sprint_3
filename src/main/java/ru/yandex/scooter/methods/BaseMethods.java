package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;

import static org.hamcrest.Matchers.notNullValue;

public class BaseMethods {


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
