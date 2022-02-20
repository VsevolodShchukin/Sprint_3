import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.PostCourier;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PostLoginTests {

    private String body400 = "{\"message\": \"Недостаточно данных для создания учетной записи\"}";
    private String body404 = "{\"message\": \"Учетная запись не найдена\"}";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }



    @Step("Send Post request to /api/v1/courier/login: Login courier")
    public Response sendPostRequestLogin(PostCourier postCourier) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(postCourier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Prepare data for post request: /api/v1/courier/login")
    public PostCourier prepareDataForPostRequestLogin(String login, String password) {
        PostCourier postCourier = new PostCourier(login, password);
        return postCourier;
    }

    @Step("Create new random courier")
    public PostCourier createRandomCourierForTest() {
        PostCourier postCourier = new PostCourier(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(postCourier)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");
        response.statusCode();
        return postCourier;
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



    @DisplayName("Для авторизации нужно передать все обязательные поля: missed login")
    @Test
    public void shouldReturnErrorWhenRequiredFieldLoginWasMissedDuringLogin() {
        PostCourier postCourier = createRandomCourierForTest();
        postCourier.setLogin(null);
        Response response = sendPostRequestLogin(postCourier);
        checkStatusCode(response, 400);
        checkBodyFromResponse(response, body400);
    }

    @DisplayName("Для авторизации нужно передать все обязательные поля: missed password")
    @Test
    public void shouldReturnErrorWhenRequiredFieldPasswordWasMissedDuringLogin() {
        PostCourier postCourier = createRandomCourierForTest();
        postCourier.setPassword(null);
        Response response = sendPostRequestLogin(postCourier);
        checkStatusCode(response, 400);
        checkBodyFromResponse(response, body400);
    }

    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Test
    public void shouldReturnErrorIfUserIsNotExistDuringLogin() {
        PostCourier postCourier = prepareDataForPostRequestLogin(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        Response response = sendPostRequestLogin(postCourier);
        checkStatusCode(response, 404);
        checkBodyFromResponse(response, body404);

    }

    @DisplayName("Cистема вернёт ошибку, если неправильно указать логин или пароль: incorrect login")
    @Test
    public void shouldReturnErrorIfFieldLoginIsIncorrectDuringLogin() {
        PostCourier postCourier = createRandomCourierForTest();
        PostCourier postCourier1 = prepareDataForPostRequestLogin(RandomStringUtils.randomAlphabetic(10), postCourier.getPassword());
        Response response = sendPostRequestLogin(postCourier1);
        checkStatusCode(response, 404);
        checkBodyFromResponse(response, body404);
    }

    @DisplayName("Cистема вернёт ошибку, если неправильно указать логин или пароль: incorrect password")
    @Test
    public void shouldReturnErrorIfFieldPasswordIsIncorrectDuringLogin() {
        PostCourier postCourier = createRandomCourierForTest();
        PostCourier postCourier1 = prepareDataForPostRequestLogin(postCourier.getLogin(), RandomStringUtils.randomAlphabetic(10));
        Response response = sendPostRequestLogin(postCourier1);
        checkStatusCode(response, 404);
        checkBodyFromResponse(response, body404);
    }

    @DisplayName("Курьер может авторизоваться")
    @Test
    public void shouldAuthorizeCourier() {
        PostCourier postCourier = createRandomCourierForTest();
        Response response = sendPostRequestLogin(postCourier);
        checkStatusCode(response, 200);
        checkBodyFromResponseWithNotNullValueCondition(response, "id");
    }






}
