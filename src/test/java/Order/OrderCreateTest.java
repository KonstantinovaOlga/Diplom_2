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

import static org.apache.http.HttpStatus.*;

public class OrderCreateTest {
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
    @DisplayName("Creating of order without token with 1 ingredient")
    @Description("Check code and answer")
    public void withoutToken_withOneIngredient() {
        this.orderApi.listHashs(1);
        Response response = this.orderApi.createOrder_WithoutToken();
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        this.methods.fieldDoesntEmpety(response,"name");
        this.methods.fieldDoesntEmpety(response,"order");
    }

    @Test
    @DisplayName("Creating of order without token with 2 ingredient")
    @Description("Check code and answer")
    public void withoutToken_withTwoIngredients() {
        this.orderApi.listHashs(2);
        Response response = this.orderApi.createOrder_WithoutToken();
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        this.methods.fieldDoesntEmpety(response,"name");
        this.methods.fieldDoesntEmpety(response,"order");
    }

    @Test
    @DisplayName("Creating of order without token with 0 ingredient")
    @Description("Check code and answer")
    public void withoutToken_withoutIngredients() {
        this.orderApi.listHashs(0);
        Response response = this.orderApi.createOrder_WithoutToken();
        this.methods.checkCode(response,SC_BAD_REQUEST);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Creating of order without token with incorrect ingredients")
    @Description("Check code and answer")
    public void withoutToken_withIncorrectIngredients() {
        this.orderApi.listHashsIncorrect(2);
        Response response = this.orderApi.createOrder_WithoutToken();
        this.methods.checkCode(response,SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Creating of order with token with 1 ingredient")
    @Description("Check code and answer")
    public void withToken_withOneIngredient() {
        this.userAPI.addUser();
        this.userAPI.defineToken();
        this.orderApi.listHashs(1);
        Response response = this.orderApi.createOrder_WithToken(this.userAPI.getToken());
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        this.methods.fieldDoesntEmpety(response,"name");
        needDelete=true;
    }

    @Test
    @DisplayName("Creating of order with token with 2 ingredient")
    @Description("Check code and answer")
    public void withToken_withTwoIngredients() {
        this.userAPI.addUser();
        this.userAPI.defineToken();
        this.orderApi.listHashs(2);
        Response response = this.orderApi.createOrder_WithToken(this.userAPI.getToken());
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        this.methods.fieldDoesntEmpety(response,"name");
        needDelete=true;
    }

    @Test
    @DisplayName("Creating of order without token with 0 ingredient")
    @Description("Check code and answer")
    public void withToken_withoutIngredients() {
        this.userAPI.addUser();
        this.userAPI.defineToken();
        this.orderApi.listHashs(0);
        Response response = this.orderApi.createOrder_WithToken(this.userAPI.getToken());
        this.methods.checkCode(response,SC_BAD_REQUEST);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"Ingredient ids must be provided");
        needDelete=true;
    }

    @Test
    @DisplayName("Creating of order with token with incorrect ingredients")
    @Description("Check code and answer")
    public void withToken_withIncorrectIngredients() {
        this.userAPI.addUser();
        this.userAPI.defineToken();
        this.orderApi.listHashsIncorrect(2);
        Response response = this.orderApi.createOrder_WithToken(this.userAPI.getToken());
        this.methods.checkCode(response,SC_INTERNAL_SERVER_ERROR);
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