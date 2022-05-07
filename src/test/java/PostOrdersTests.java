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
import ru.yandex.scooter.methods.PostOrdersMethods;
import ru.yandex.scooter.models.PostOrdersModel;


@RunWith(Parameterized.class)
public class PostOrdersTests {

    PostOrdersMethods method = new PostOrdersMethods();

    private final PostOrdersModel order;

    public PostOrdersTests(PostOrdersModel order) {
        this.order = order;
    }

    @Step("Prepare data for post request: /api/v1/orders")
    @Parameterized.Parameters
    public static Object[][] getData() {
        String randomStringData = RandomStringUtils.randomAlphabetic(10);
        int randomIntData = RandomUtils.nextInt(1, 100);
        String randomNumericStringData = RandomStringUtils.randomNumeric(5);
        return new Object[][]{
                {new PostOrdersModel(randomStringData, randomStringData, randomStringData, randomIntData, randomNumericStringData, randomIntData, randomNumericStringData, randomStringData, new String[]{"BLACK"})},
                {new PostOrdersModel(randomStringData, randomStringData, randomStringData, randomIntData, randomNumericStringData, randomIntData, randomNumericStringData, randomStringData, new String[]{"GREY"})},
                {new PostOrdersModel(randomStringData, randomStringData, randomStringData, randomIntData, randomNumericStringData, randomIntData, randomNumericStringData, randomStringData, new String[]{"BLACK", "GREY"})},
                {new PostOrdersModel(randomStringData, randomStringData, randomStringData, randomIntData, randomNumericStringData, randomIntData, randomNumericStringData, randomStringData, null)}
        };
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }



    @Test
    @DisplayName("Должен создавать новый заказ")
    public void shouldCreateNewOrder() {
        Response response = method.sendPostRequestOrders(order);
        method.checkStatusCode(response, 201);
        method.checkBodyFromResponseWithNotNullValueCondition(response, "track");
    }







}
