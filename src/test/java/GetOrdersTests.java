import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.scooter.methods.GetOrdersMethods;


@RunWith(Parameterized.class)
public class GetOrdersTests {

    GetOrdersMethods method = new GetOrdersMethods();

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


    @DisplayName("В тело ответа возвращается список заказов")
    @Test
    public void shouldReturnListOfOrders() {
        Response response = method.sendGetRequestOrders(param);
        method.checkStatusCode(response, 200);
        method.checkBodyFromResponseWithNotNullValueCondition(response, "orders");
        System.out.println(response.body().asString());
    }
}
