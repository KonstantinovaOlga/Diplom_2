package config;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class MethodOfChecking {

    @Step("Check code of answer")
    public void checkCode(Response response, int code)  {
        response.then().assertThat().statusCode(code);
    }

    @Step("Answer has needed field")
    public void fieldDoesntEmpety(Response response, String field)  {
        response.then().assertThat().body(field, notNullValue());
    }

    @Step("Check message")
    public void checkMessage(Response response, String message)  {
        response.then().assertThat().body("message", equalTo(message));
    }
    @Step("Check boolean value of the field")
    public void checkFieldBooleanValue(Response response, String field, Boolean value)  {
        response.then().assertThat().body(field, equalTo(value));
    }

}
