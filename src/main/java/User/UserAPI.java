package User;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserAPI {
    public static final String CREATE_USER = "auth/register";
    public static final String LOGIN_USER = "auth/login";
    public static final String UPDATE_USER = "auth/user";

    private String token;


    private final UserObj user;

    public UserAPI (UserObj user) {
        this.user=user;
    }

    @Step("Creating of user")
    public Response addUser() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post(CREATE_USER);
        return response;
    }

    @Step("User Login")
    public Response loginUser() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post(LOGIN_USER);
        return response;
    }

    @Step("Define token")
    public void defineToken() {
        String token =
                given().
                        header("Content-type", "application/json").
                        and().
                        body(user).
                        when().
                        post(LOGIN_USER).
                        then().
                        statusCode(200).
                        extract().
                        path("accessToken");

        this.token=token.substring(7);
    }

    @Step("Delete User")
    public Response deleteUser() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(token)
                        .and()
                        .body(user)
                        .when()
                        .post(LOGIN_USER);
        return response;
    }

    @Step("Update User with token - get response")
    public Response updateUserWithToken_Response() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(token)
                        .and()
                        .body(user)
                        .when()
                        .patch(UPDATE_USER);
        return response;
    }

    @Step("Update User with token - get result")
    public UserResultUpdating updateUserWithToken_Object() {
        UserResultUpdating result =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .auth().oauth2(token)
                        .and()
                        .body(user)
                        .when()
                        .patch(UPDATE_USER)
                        .body().as(UserResultUpdating.class);
        return result;
    }

    @Step("Update User without token")
    public Response updateUserWithoutToken() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .patch(UPDATE_USER);
        return response;
    }

    public String getToken() {
        return token;
    }
}
