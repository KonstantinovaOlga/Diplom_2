package User;

import config.MethodOfChecking;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class UserCreateTest {

    private UserObj user;
    private UserAPI userAPI;
    private boolean needDelete;
    private MethodOfChecking methods;

    @Before
    public void setUp() {
        RestAssured.baseURI = config.URL.URL;
        this.user = new UserObj();
        this.user = this.user.getRandomUser();
        this.userAPI = new UserAPI(this.user);
        needDelete = false;
        this.methods=new MethodOfChecking();
    }

    @Test
    @DisplayName("Creating of user")
    @Description("Check code and answer")
    public void createNewUser() {
        Response response = this.userAPI.addUser();
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        this.methods.fieldDoesntEmpety(response,"accessToken");
        this.methods.fieldDoesntEmpety(response,"refreshToken");
        needDelete = true;
    }

    @Test
    @DisplayName("Creating double of user")
    @Description("Check code, answer and message")
    public void createDoubleUser() {
        userAPI.addUser();
        Response responseRepeat = userAPI.addUser();
        this.methods.checkCode(responseRepeat,SC_FORBIDDEN);
        this.methods.checkFieldBooleanValue(responseRepeat,"success",false);
        this.methods.checkMessage(responseRepeat,"User already exists");
        needDelete = true;
    }

    @Test
    @DisplayName("Creating of user with null email")
    @Description("Check code, answer and message")
    public void createNewUserWithNullPassword() {
        this.user.setEmail(null);
        UserAPI userAPIObj = new UserAPI(this.user);
        Response response = userAPIObj.addUser();
        this.methods.checkCode(response,SC_FORBIDDEN);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"Email, password and name are required fields");
        needDelete = false;
    }

    @Test
    @DisplayName("Creating of user with null password")
    @Description("Check code, answer and message")
    public void createNewUserWithNullEmail() {
        this.user.setPassword(null);
        UserAPI userAPIObj = new UserAPI(this.user);
        Response response = userAPIObj.addUser();
        this.methods.checkCode(response,SC_FORBIDDEN);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"Email, password and name are required fields");
        needDelete = false;
    }

    @Test
    @DisplayName("Creating of user with null Name")
    @Description("Check code, answer and message")
    public void createNewUserWithNullName() {
        this.user.setName(null);
        UserAPI userAPIObj = new UserAPI(this.user);
        Response response = userAPIObj.addUser();
        this.methods.checkCode(response,SC_FORBIDDEN);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"Email, password and name are required fields");
        needDelete = false;
    }

    @After
    public void tearDown() {
        if (needDelete) {
            this.userAPI.defineToken();
            userAPI.deleteUser();
        }
    }
}