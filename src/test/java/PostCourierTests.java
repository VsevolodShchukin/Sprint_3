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


public class PostCourierTests {


    private final String body201 = "{ok: true}";
    private final String body400 = "{\"message\": \"Недостаточно данных для создания учетной записи\"}";
    private final String body409 = "{\"message\": \"Этот логин уже используется\"}";

    PostCourierMethods method = new PostCourierMethods();
    PostCourierModel courierForTest;
    String bodyCourierForTest;

    @Before
    public void setUp() {
        System.out.println("Test setUp");
        String randomData = RandomStringUtils.randomAlphabetic(10);
        courierForTest = new PostCourierModel(randomData, randomData, randomData);
    }

    @After
    public void tearDown() {
        System.out.println("Test tearDown");
        PostLoginMethods loginMethods = new PostLoginMethods();
        bodyCourierForTest = loginMethods.sendPostRequestLogin(courierForTest).body().asString();
        String idCourier = bodyCourierForTest.replace("}", "").split(":")[1];
        BaseMethods.sendDeleteRequest(bodyCourierForTest, "/api/v1/courier/" + idCourier);
    }



    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed login")
    @Test
    public void shouldReturnErrorWhenRequiredFieldLoginWasMissedDuringCreation() {
        courierForTest.setLogin(null);
        Response response = method.sendPostRequestCourier(courierForTest);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed password")
    @Test
    public void shouldReturnErrorWhenRequiredFieldPasswordWasMissedDuringCreation() {
        courierForTest.setPassword(null);
        Response response = method.sendPostRequestCourier(courierForTest);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Чтобы создать курьера, нужно передать в ручку все обязательные поля: missed firstName")
    @Test
    public void shouldReturnErrorWhenRequiredFieldFirstNameWasMissedDuringCreation() {
        courierForTest.setFirstName(null);
        Response response = method.sendPostRequestCourier(courierForTest);
        method.checkStatusCode(response, 400);
        method.checkBodyFromResponse(response, body400);
    }

    @DisplayName("Курьера можно создать")
    @Test
    public void shouldCreateNewCourier() {
        Response response = method.sendPostRequestCourier(courierForTest);
        method.checkStatusCode(response,201);
        method.checkBodyFromResponse(response, body201);
    }

    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Test
    public void shouldBeUnableToCreateTwoIdenticalCouriers() {
        Response response1 = method.sendPostRequestCourier(courierForTest);
        method.checkStatusCode(response1,201);
        Response response2 = method.sendPostRequestCourier(courierForTest);
        method.checkStatusCode(response2,409);
        method.checkBodyFromResponse(response2, body409);
    }

    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    @Test
    public void shouldReturnErrorWhenLoginIsAlreadyUsedDuringCreation() {
        String login = RandomStringUtils.randomAlphabetic(10);
        Response response1 = method.sendPostRequestCourier(courierForTest);
        method.checkStatusCode(response1,201);
        PostCourierModel postCourier = new PostCourierModel(login, courierForTest.getPassword(), courierForTest.getFirstName());
        Response response2 = method.sendPostRequestCourier(postCourier);
        method.checkStatusCode(response2, 409);
        method.checkBodyFromResponse(response2, body409);
    }

}
