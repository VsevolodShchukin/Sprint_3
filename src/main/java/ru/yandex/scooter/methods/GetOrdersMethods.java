package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;


public class GetOrdersMethods extends BaseMethods{

    private final String url = "/api/v1/orders";

    @Step("Send Get request to /api/v1/orders: Get orders")
    public Response sendGetRequestOrders(String param) {
        Response response = sendGetRequest(param, url);
        return response;
    }

}
