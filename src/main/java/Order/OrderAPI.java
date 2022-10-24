package Order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class OrderAPI {
    public static final String LIST_INGREDIENTS = "ingredients";
    public static final String CREATE_ORDER= "orders";
    public static final String LIST_ORDERS= "orders";
    private ListIngredients listIngredients;
    private ListHashs listHashs;
    private ListOrders listOrders;


    public OrderAPI() {
    }

    public OrderAPI(ListIngredients listIngredients, ListHashs listHashs, ListOrders listOrders) {
        this.listIngredients = listIngredients;
        this.listHashs = listHashs;
        this.listOrders = listOrders;
    }

    @Step("Get list of ingredients")
    public Response listIngredients() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get(LIST_INGREDIENTS);
          this.listIngredients = response.body().as(ListIngredients.class);
          return response;
    }

    @Step("Generate list of ingredients")
    public void listHashs(int amountIngredients) {
        this.listHashs = new ListHashs();
        int size = this.listIngredients.getIngredients().size();
        Random random = new Random();
        int randNum = 0;
        for (int i=0;i<amountIngredients; i=i+1) {
            randNum = random.nextInt(size);
            this.listHashs.addHash(this.listIngredients.getIngredients().get(randNum).get_id());
        }
    }

    @Step("Generate incorrect list of ingredients")
    public void listHashsIncorrect(int amountIngredients) {
        this.listHashs = new ListHashs();
        for (int i=0;i<amountIngredients; i=i+1) {
            this.listHashs.addHash(RandomStringUtils.randomAlphanumeric(10));
        }
    }

    @Step("Create order without token")
    public Response createOrder_WithoutToken() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(listHashs)
                        .when()
                        .post(CREATE_ORDER);
        return response;
    }

    @Step("Create order with token")
    public Response createOrder_WithToken (String token) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(token)
                        .and()
                        .body(listHashs)
                        .when()
                        .post(CREATE_ORDER);
        return response;
    }

    @Step("Get list of orders without token")
    public Response listOrdersWithoutToken() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get(LIST_ORDERS);
        this.listOrders = response.body().as(ListOrders.class);
        return response;
    }

    @Step("Get list of orders with token")
    public Response listOrdersWithToken(String token) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(token)
                        .when()
                        .get(LIST_ORDERS);
        this.listOrders = new ListOrders();
        this.listOrders = response.body().as(ListOrders.class);
        return response;
    }

    public ListOrders getListOrders() {
        return listOrders;
    }
}
