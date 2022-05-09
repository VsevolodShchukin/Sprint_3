package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.scooter.models.PostCourierModel;


public class PostLoginMethods extends BaseMethods {

    private final String url = "/api/v1/courier/login";

    @Step("Send Post request to /api/v1/courier/login: Login courier")
    public Response sendPostRequestLogin(PostCourierModel postCourier) {
        Response response = sendPostRequest(postCourier, url);
        return response;
    }

}