package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetOrdersMethods extends BaseMethods{

    @Step("Send Get request to /api/v1/orders: Get orders")
    public Response sendGetRequestOrders(String param) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .param(param)
                .get("/api/v1/orders");
        return response;
    }

}
