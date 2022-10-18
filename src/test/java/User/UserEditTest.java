package User;

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

public class UserEditTest {
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
    @DisplayName("Updating of user without token")
    @Description("Check code and message")
    public void withoutToken() {
        userAPI.addUser();
        Response response = userAPI.updateUserWithoutToken();
        this.methods.checkCode(response,SC_UNAUTHORIZED);
        this.methods.checkFieldBooleanValue(response,"success",false);
        this.methods.checkMessage(response,"You should be authorised");
        needDelete = true;
    }

    @Test
    @DisplayName("Updating of user with token - change Email and Name")
    @Description("Check code and answer")
    public void withToken_changeEmailAndName() {
        this.userAPI.addUser();
        this.userAPI.defineToken();
        String newEmail = this.user.getEmail()+"1";
        String newName = this.user.getName()+"1";
        this.user.setEmail(newEmail);
        this.user.setName(newName);
        Response response = userAPI.updateUserWithToken_Response();
        UserResultUpdating result = userAPI.updateUserWithToken_Object();
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        assertEquals(newEmail.toLowerCase(),result.user.getEmail());
        assertEquals(newName,result.user.getName());
        needDelete = true;
    }

    @Test
    @DisplayName("Updating of user with token - change Email")
    @Description("Check code and answer")
    public void withToken_changeEmail() {
        this.userAPI.addUser();
        this.userAPI.defineToken();
        String newEmail = this.user.getEmail()+"1";
        this.user.setEmail(newEmail);
        Response response = userAPI.updateUserWithToken_Response();
        UserResultUpdating result = userAPI.updateUserWithToken_Object();
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        assertEquals(newEmail.toLowerCase(),result.user.getEmail());
        needDelete = true;
    }

    @Test
    @DisplayName("Updating of user with token - change Name")
    @Description("Check code and answer")
    public void withToken_changeName() {
        this.userAPI.addUser();
        this.userAPI.defineToken();
        String newName = this.user.getName()+"1";
        this.user.setName(newName);
        Response response = userAPI.updateUserWithToken_Response();
        UserResultUpdating result = userAPI.updateUserWithToken_Object();
        this.methods.checkCode(response,SC_OK);
        this.methods.checkFieldBooleanValue(response,"success",true);
        assertEquals(newName,result.user.getName());
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
