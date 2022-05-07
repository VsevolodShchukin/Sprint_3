import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.methods.PostLoginMethods;
import ru.yandex.scooter.models.PostCourierModel;


public class PostLoginTests {

    private String body400 = "{\"message\": \"Недостаточно данных для создания учетной записи\"}";
    private String body404 = "{\"message\": \"Учетная запись не найдена\"}";
    PostLoginMethods method = new PostLoginMethods();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @DisplayName("Для авторизации нужно передать все обязательные поля: missed login")
    @Test
    public void shouldReturnErrorWhenRequiredFieldLoginWasMissedDuringLogin() {
        PostCourierModel postCourier = method.createRandomCourierForTest();
        postCourier.setLogin(null);
        Response response = method.sendPostRequestLogin(postCourier);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Для авторизации нужно передать все обязательные поля: missed password")
    @Test
    public void shouldReturnErrorWhenRequiredFieldPasswordWasMissedDuringLogin() {
        PostCourierModel postCourier = method.createRandomCourierForTest();
        postCourier.setPassword(null);
        Response response = method.sendPostRequestLogin(postCourier);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Test
    public void shouldReturnErrorIfUserIsNotExistDuringLogin() {
        PostCourierModel postCourier = method.prepareDataForPostRequestLogin(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        Response response = method.sendPostRequestLogin(postCourier);
        method.checkStatusCode(response, 404);
        method.checkBodyFromResponse(response, body404);

    }

    @DisplayName("Cистема вернёт ошибку, если неправильно указать логин или пароль: incorrect login")
    @Test
    public void shouldReturnErrorIfFieldLoginIsIncorrectDuringLogin() {
        PostCourierModel postCourier = method.createRandomCourierForTest();
        PostCourierModel postCourier1 = method.prepareDataForPostRequestLogin(RandomStringUtils.randomAlphabetic(10), postCourier.getPassword());
        Response response = method.sendPostRequestLogin(postCourier1);
        method.checkStatusCode(response, 404);
        method.checkBodyFromResponse(response, body404);
    }

    @DisplayName("Cистема вернёт ошибку, если неправильно указать логин или пароль: incorrect password")
    @Test
    public void shouldReturnErrorIfFieldPasswordIsIncorrectDuringLogin() {
        PostCourierModel postCourier = method.createRandomCourierForTest();
        PostCourierModel postCourier1 = method.prepareDataForPostRequestLogin(postCourier.getLogin(), RandomStringUtils.randomAlphabetic(10));
        Response response = method.sendPostRequestLogin(postCourier1);
        method.checkStatusCode(response, 404);
        method.checkBodyFromResponse(response, body404);
    }

    @DisplayName("Курьер может авторизоваться")
    @Test
    public void shouldAuthorizeCourier() {
        PostCourierModel postCourier = method.createRandomCourierForTest();
        Response response = method.sendPostRequestLogin(postCourier);
        method.checkStatusCode(response, 200);
        method.checkBodyFromResponseWithNotNullValueCondition(response, "id");
    }

}
