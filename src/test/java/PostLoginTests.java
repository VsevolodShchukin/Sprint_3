import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.methods.BaseMethods;
import ru.yandex.scooter.methods.PostCourierMethods;
import ru.yandex.scooter.methods.PostLoginMethods;
import ru.yandex.scooter.models.PostCourierModel;


public class PostLoginTests {

    private final String body400 = "{\"message\":  \"Недостаточно данных для входа\"}";
    private final String body404 = "{\"message\": \"Учетная запись не найдена\"}";

    PostLoginMethods method = new PostLoginMethods();
    PostCourierModel courierForTest;
    String bodyCourierForTest;

    @Before
    public void setUp() {
        System.out.println("Test setUp");
        PostCourierMethods courierMethod = new PostCourierMethods();
        String randomData = RandomStringUtils.randomAlphabetic(10);
        PostCourierModel postCourier = new PostCourierModel(randomData,randomData,randomData);
        courierMethod.sendPostRequestCourier(postCourier).statusCode();
        courierForTest = postCourier;
        bodyCourierForTest = method.sendPostRequestLogin(courierForTest).body().asString();
    }

    @After
    public void tearDown() {
        System.out.println("Test tearDown");
        String idCourier =  bodyCourierForTest.replace("}", "").split(":")[1];
        BaseMethods.sendDeleteRequest(bodyCourierForTest, "/api/v1/courier/" + idCourier);
    }


    @DisplayName("Для авторизации нужно передать все обязательные поля: missed login")
    @Test
    public void shouldReturnErrorWhenRequiredFieldLoginWasMissedDuringLogin() {
        courierForTest.setLogin(null);
        Response response = method.sendPostRequestLogin(courierForTest);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Для авторизации нужно передать все обязательные поля: missed password")
    @Test
    public void shouldReturnErrorWhenRequiredFieldPasswordWasMissedDuringLogin() {
        courierForTest.setPassword(null);
        Response response = method.sendPostRequestLogin(courierForTest);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Test
    public void shouldReturnErrorIfUserIsNotExistDuringLogin() {
        String randomData = RandomStringUtils.randomAlphabetic(10);
        PostCourierModel postCourier = new PostCourierModel(randomData, randomData);
        Response response = method.sendPostRequestLogin(postCourier);
        method.checkStatusCode(response, 404);
        method.checkBodyFromResponse(response, body404);

    }

    @DisplayName("Система вернёт ошибку, если неправильно указать логин или пароль: incorrect login")
    @Test
    public void shouldReturnErrorIfFieldLoginIsIncorrectDuringLogin() {
        courierForTest.setLogin("Unexpected");
        Response response = method.sendPostRequestLogin(courierForTest);
        method.checkStatusCode(response, 404);
        method.checkBodyFromResponse(response, body404);
    }

    @DisplayName("Система вернёт ошибку, если неправильно указать логин или пароль: incorrect password")
    @Test
    public void shouldReturnErrorIfFieldPasswordIsIncorrectDuringLogin() {
        courierForTest.setPassword("Unexpected");
        Response response = method.sendPostRequestLogin(courierForTest);
        method.checkStatusCode(response, 404);
        method.checkBodyFromResponse(response, body404);
    }

    @DisplayName("Курьер может авторизоваться")
    @Test
    public void shouldAuthorizeCourier() {
        Response response = method.sendPostRequestLogin(courierForTest);
        method.checkStatusCode(response, 200);
        method.checkBodyFromResponseWithNotNullValueCondition(response, "id");
    }

}
