package ru.yandex.scooter.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.scooter.models.PostCourierModel;

import static io.restassured.RestAssured.given;

public class PostCourierMethods extends BaseMethods {


    @Step("Send Post request to /api/v1/courier: Create courier")
    public Response sendPostRequestCourier(PostCourierModel postCourier) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(postCourier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Prepare data for post request: /api/v1/courier/courier")
    public PostCourierModel prepareDataForPostRequestCourier(String login, String password, String firstName) {
        PostCourierModel postCourier = new PostCourierModel(login, password, firstName);
        return postCourier;
    }

    @Step("Prepare data for post request: /api/v1/courier/courier")
    public PostCourierModel prepareRandomDataForPostRequestCourier(String randomData) {
        PostCourierModel postCourier = new PostCourierModel(randomData, randomData, randomData);
        return postCourier;
    }


}
