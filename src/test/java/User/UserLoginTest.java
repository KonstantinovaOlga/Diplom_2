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

public class UserLoginTest {

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
    @DisplayName("Login of user")
    @Description("Check code and getting of tokens")
    public void loginUser() {
        userAPI.addUser();
        Response response = userAPI.loginUser();
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        this.methods.fieldDoesntEmpety(response,"accessToken");
        this.methods.fieldDoesntEmpety(response,"refreshToken");
        needDelete = true;
    }

    @Test
    @DisplayName("Login of user with incorrect email")
    @Description("Check code and message")
    public void loginWithIncorrectEmail() {
        userAPI.addUser();
        UserObj UserSecond = new UserObj(this.user.getEmail() + "1",
                this.user.getPassword(),
                this.user.getName());
        UserAPI userAPISecond = new UserAPI(UserSecond);
        Response response = userAPISecond.loginUser();
        this.methods.checkCode(response,SC_UNAUTHORIZED);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"email or password are incorrect");
        needDelete = true;
    }

    @Test
    @DisplayName("Login of user with incorrect password")
    @Description("Check code and message")
    public void loginWithIncorrectLogin() {
        userAPI.addUser();
        UserObj UserSecond = new UserObj(this.user.getEmail(),
                this.user.getPassword() + "1",
                this.user.getName());
        UserAPI userAPISecond = new UserAPI(UserSecond);
        Response response = userAPISecond.loginUser();
        this.methods.checkCode(response,SC_UNAUTHORIZED);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"email or password are incorrect");
        needDelete = true;
    }

    @Test
    @DisplayName("Login of user with unknown data")
    @Description("Check code and message")
    public void loginWithUnknownData() {
        Response response = userAPI.loginUser();
        this.methods.checkCode(response,SC_UNAUTHORIZED);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"email or password are incorrect");
        needDelete = false;
    }

    @Test
    @DisplayName("Login of user with null email")
    @Description("Check code and message")
    public void loginWithNullEmail() {
        userAPI.addUser();
        UserObj userSecond = new UserObj(null,
                this.user.getPassword(),
                this.user.getName());
        UserAPI userAPISecond = new UserAPI(userSecond);
        Response response = userAPISecond.loginUser();
        this.methods.checkCode(response,SC_UNAUTHORIZED);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"email or password are incorrect");
        needDelete = true;
    }

    @Test
    @DisplayName("Login of user with null password")
    @Description("Check code and message")
    public void loginWithNullPassword() {
        userAPI.addUser();
        UserObj userSecond = new UserObj(this.user.getEmail(),
                null,
                this.user.getName());
        UserAPI userAPISecond = new UserAPI(userSecond);
        Response response = userAPISecond.loginUser();
        this.methods.checkCode(response,SC_UNAUTHORIZED);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"email or password are incorrect");
        needDelete = true;
    }

    @After
    public void tearDown() {
        if (needDelete) {
            this.userAPI.defineToken();
            userAPI.deleteUser();
        }
    }
}
