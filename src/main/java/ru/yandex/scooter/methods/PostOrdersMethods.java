package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.scooter.models.BasePost;
import ru.yandex.scooter.models.PostOrdersModel;


public class PostOrdersMethods extends BaseMethods implements BasePost {

    private final String url = "/api/v1/orders";

    @Step("Send Post request to /api/v1/orders: Create order")
    public Response sendPostRequestOrders(PostOrdersModel postOrder) {
        Response response = sendPostRequest(postOrder, url);
        return response;
    }

}
