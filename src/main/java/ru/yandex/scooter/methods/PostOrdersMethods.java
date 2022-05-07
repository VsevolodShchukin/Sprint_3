package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.scooter.models.PostOrdersModel;

import static io.restassured.RestAssured.given;


public class PostOrdersMethods extends BaseMethods {

    @Step("Send Post request to /api/v1/orders: Create order")
    public Response sendPostRequestOrders(PostOrdersModel order) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
        return response;
    }


}
