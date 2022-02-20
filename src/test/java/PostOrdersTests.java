import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.scooter.PostOrders;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class PostOrdersTests {

    private final PostOrders order;

    public PostOrdersTests(PostOrders order) {
        this.order = order;
    }

    @Step("Prepare data for post request: /api/v1/orders")
    @Parameterized.Parameters
    public static Object[][] getData() {
        String randomStringData = RandomStringUtils.randomAlphabetic(10);
        int randomIntData = RandomUtils.nextInt(1, 100);
        String randomNumericStringData = RandomStringUtils.randomNumeric(5);
        return new Object[][]{
                {new PostOrders(randomStringData, randomStringData, randomStringData, randomIntData, randomNumericStringData, randomIntData, randomNumericStringData, randomStringData, new String[]{"BLACK"})},
                {new PostOrders(randomStringData, randomStringData, randomStringData, randomIntData, randomNumericStringData, randomIntData, randomNumericStringData, randomStringData, new String[]{"GREY"})},
                {new PostOrders(randomStringData, randomStringData, randomStringData, randomIntData, randomNumericStringData, randomIntData, randomNumericStringData, randomStringData, new String[]{"BLACK", "GREY"})},
                {new PostOrders(randomStringData, randomStringData, randomStringData, randomIntData, randomNumericStringData, randomIntData, randomNumericStringData, randomStringData, null)}
        };
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Send Post request to /api/v1/orders: Create order")
    public Response sendPostRequestOrders(PostOrders order) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
        return response;
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Check body from response")
    public void checkBodyFromResponseWithNotNullValueCondition(Response response, String key) {
        response.then().assertThat().body(key, notNullValue());
    }

    @Test
    @DisplayName("Должен создавать новый заказ")
    public void shouldCreateNewOrder() {
        Response response = sendPostRequestOrders(order);
        checkStatusCode(response, 201);
        checkBodyFromResponseWithNotNullValueCondition(response, "track");
    }







}
