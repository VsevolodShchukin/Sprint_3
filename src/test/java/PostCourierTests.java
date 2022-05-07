import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.methods.PostCourierMethods;
import ru.yandex.scooter.models.PostCourierModel;


public class PostCourierTests {


    private String body201 = "{ok: true}";
    private String body400 = "{\"message\": \"Недостаточно данных для создания учетной записи\"}";
    private String body409 = "{\"message\": \"Этот логин уже используется\"}";
    PostCourierMethods method = new PostCourierMethods();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed login")
    @Test
    public void shouldReturnErrorWhenRequiredFieldLoginWasMissedDuringCreation() {
        PostCourierModel postCourier = method.prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        postCourier.setLogin(null);
        Response response = method.sendPostRequestCourier(postCourier);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed password")
    @Test
    public void shouldReturnErrorWhenRequiredFieldPasswordWasMissedDuringCreation() {
        PostCourierModel postCourier = method.prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        postCourier.setPassword(null);
        Response response = method.sendPostRequestCourier(postCourier);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed firstName")
    @Test
    public void shouldReturnErrorWhenRequiredFieldFirstNameWasMissedDuringCreation() {
        PostCourierModel postCourier = method.prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        postCourier.setFirstName(null);
        Response response = method.sendPostRequestCourier(postCourier);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Курьера можно создать")
    @Test
    public void shouldCreateNewCourier() {
        PostCourierModel postCourier = method.prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        Response response = method.sendPostRequestCourier(postCourier);
        method.checkStatusCode(response,201);
        method.checkBodyFromResponse(response, body201);
    }

    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Test
    public void shouldBeUnableToCreateTwoIdenticalCouriers() {
        PostCourierModel postCourier = method.prepareRandomDataForPostRequestCourier(RandomStringUtils.randomAlphabetic(10));
        Response response1 = method.sendPostRequestCourier(postCourier);
        method.checkStatusCode(response1, 201);
        Response response2 = method.sendPostRequestCourier(postCourier);
        method.checkStatusCode(response2,409);
        method.checkBodyFromResponse(response2, body409);
    }

    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    @Test
    public void shouldReturnErrorWhenLoginIsAlreadyUsedDuringCreation() {
        String login = RandomStringUtils.randomAlphabetic(10);
        PostCourierModel postCourier1 = method.prepareDataForPostRequestCourier(login, RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        PostCourierModel postCourier2 = method.prepareDataForPostRequestCourier(login, RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        Response response1 = method.sendPostRequestCourier(postCourier1);
        method.checkStatusCode(response1, 201);
        Response response2 = method.sendPostRequestCourier(postCourier2);
        method.checkStatusCode(response2, 409);
        method.checkBodyFromResponse(response2, body409);
    }

}
