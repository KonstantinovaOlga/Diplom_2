package Order;

import User.UserAPI;
import User.UserObj;
import config.MethodOfChecking;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class OrderListTest {
    private OrderAPI orderApi;
    private MethodOfChecking methods;
    private UserObj user;
    private UserAPI userAPI;
    private boolean needDelete;

    @Before
    public void setUp() {
        RestAssured.baseURI = config.URL.URL;
        this.user = new UserObj();
        this.user = this.user.getRandomUser();
        this.userAPI = new UserAPI(this.user);
        this.orderApi = new OrderAPI();
        this.orderApi.listIngredients();
        this.methods=new MethodOfChecking();
        needDelete=false;
    }

    @Test
    @DisplayName("Get list of orders without token")
    @Description("Check code and answer")
    public void withoutToken() {
        Response response = this.orderApi.listOrdersWithoutToken();
        this.methods.checkCode(response,SC_UNAUTHORIZED);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"You should be authorised");
    }

    @Test
    @DisplayName("Get list of orders with token")
    @Description("Check code and answer")
    public void withToken() {
        this.userAPI.addUser();
        this.userAPI.defineToken();

        this.orderApi.listHashs(2);
        this.orderApi.createOrder_WithToken(this.userAPI.getToken());

        this.orderApi.listHashs(3);
        this.orderApi.createOrder_WithToken(this.userAPI.getToken());

        Response response = this.orderApi.listOrdersWithToken(this.userAPI.getToken());
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        int actualNumber = this.orderApi.getListOrders().getOrders().size();
        int expectedNumber = 2;
        assertEquals(actualNumber, expectedNumber);
        needDelete=true;
    }

    @Test
    @DisplayName("Get list of orders with token")
    @Description("Check total and totalToday")
    public void withToken_checkTotal() {
        this.userAPI.addUser();
        this.userAPI.defineToken();

        this.orderApi.listHashs(2);
        this.orderApi.createOrder_WithToken(this.userAPI.getToken());

        this.orderApi.listHashs(3);
        this.orderApi.createOrder_WithToken(this.userAPI.getToken());

        Response response = this.orderApi.listOrdersWithToken(this.userAPI.getToken());
        int actualTotal = this.orderApi.getListOrders().getTotal();
        int actualTotalToday = this.orderApi.getListOrders().getTotalToday();
        int expected = 2;
        assertEquals(actualTotal, expected);
        assertEquals(actualTotalToday, expected);
        needDelete=true;
    }

    @After
    public void tearDown() {
        if (needDelete) {
            this.userAPI.defineToken();
            userAPI.deleteUser();
        }
    }
}
