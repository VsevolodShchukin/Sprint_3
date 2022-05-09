package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.scooter.models.PostCourierModel;

public class PostCourierMethods extends BaseMethods {

    private final String url = "/api/v1/courier";

    @Step("Send Post request to /api/v1/courier: Create courier")
    public Response sendPostRequestCourier(PostCourierModel postCourier) {
        Response response = sendPostRequest(postCourier, url);
        return response;
    }

}
