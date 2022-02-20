import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.PostCourier;


import java.lang.reflect.Type;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class PostCourierTests {

    private String body201 = "{ok: true}";
    private String body400 = "{\"message\": \"Недостаточно данных для создания учетной записи\"}";
    private String body409 = "{\"message\": \"Этот логин уже используется\"}";


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Step("Send Post request to /api/v1/courier: Create courier")
    public Response sendPostRequestCourier(PostCourier postCourier) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(postCourier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Prepare data for post request: /api/v1/courier/courier")
    public PostCourier prepareDataForPostRequestCourier(String login, String password, String firstName) {
        PostCourier postCourier = new PostCourier(login, password, firstName);
        return postCourier;
    }

    @Step("Prepare data for post request: /api/v1/courier/courier")
    public PostCourier prepareRandomDataForPostRequestCourier(String randomData) {
        PostCourier postCourier = new PostCourier(randomData, randomData, randomData);
        return postCourier;
    }

    @Step("Check body from response")
    public void checkBodyFromResponse(Response response, String expectedBody) {
        String responseBody = response.body().asString();
        Assert.assertEquals(expectedBody, responseBody);
    }





    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed login")
    @Test
    public void shouldReturnErrorWhenRequiredFieldLoginWasMissedDuringCreation() {
        PostCourier postCourier = prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        postCourier.setLogin(null);
        Response response = sendPostRequestCourier(postCourier);
        checkStatusCode(response, 400);
        checkBodyFromResponse(response, body400);
    }

    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed password")
    @Test
    public void shouldReturnErrorWhenRequiredFieldPasswordWasMissedDuringCreation() {
        PostCourier postCourier = prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        postCourier.setPassword(null);
        Response response = sendPostRequestCourier(postCourier);
        checkStatusCode(response, 400);
        checkBodyFromResponse(response, body400);
    }

    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed firstName")
    @Test
    public void shouldReturnErrorWhenRequiredFieldFirstNameWasMissedDuringCreation() {
        PostCourier postCourier = prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        postCourier.setFirstName(null);
        Response response = sendPostRequestCourier(postCourier);
        checkStatusCode(response, 400);
        checkBodyFromResponse(response, body400);
    }

    @DisplayName("Курьера можно создать")
    @Test
    public void shouldCreateNewCourier() {
        PostCourier postCourier = prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        Response response = sendPostRequestCourier(postCourier);
        checkStatusCode(response,201);
        checkBodyFromResponse(response, body201);
    }

    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Test
    public void shouldBeUnableToCreateTwoIdenticalCouriers() {
        PostCourier postCourier = prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        Response response1 = sendPostRequestCourier(postCourier);
        checkStatusCode(response1, 201);
        Response response2 = sendPostRequestCourier(postCourier);
        checkStatusCode(response2,409);
        checkBodyFromResponse(response2, body409);
    }

    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    @Test
    public void shouldReturnErrorWhenLoginIsAlreadyUsedDuringCreation() {
        String login = RandomStringUtils.randomAlphabetic(10);
        PostCourier postCourier1 = prepareDataForPostRequestCourier(login, RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        PostCourier postCourier2 = prepareDataForPostRequestCourier(login, RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        Response response1 = sendPostRequestCourier(postCourier1);
        checkStatusCode(response1, 201);
        Response response2 = sendPostRequestCourier(postCourier2);
        checkStatusCode(response2, 409);
        checkBodyFromResponse(response2, body409);
    }

}
