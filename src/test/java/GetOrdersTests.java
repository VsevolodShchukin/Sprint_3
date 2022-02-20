import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(Parameterized.class)
public class GetOrdersTests {


    private final String param;

    public GetOrdersTests(String param) {
        this.param = param;
    }

    @Step("Prepare data for get request: /api/v1/orders")
    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"?courierId=1"},
                {"?courierId=1&nearestStation=[\"1\", \"2\"]"},
                {"?limit=10&page=0"},
                {"?limit=10&page=0&nearestStation=[\"110\"]"}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Send Get request to /api/v1/orders: Get orders")
    public Response sendGetRequestOrders(String param) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .param(param)
                .get("/api/v1/orders");
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

    @DisplayName("В тело ответа возвращается список заказов")
    @Test
    public void shouldReturnListOfOrders() {
        Response response = sendGetRequestOrders(param);
        checkStatusCode(response, 200);
        checkBodyFromResponseWithNotNullValueCondition(response, "orders");
        System.out.println(response.body().asString());
    }
}
