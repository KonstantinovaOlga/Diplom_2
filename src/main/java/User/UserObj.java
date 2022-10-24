package User;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserObj {
    private String email;
    private String password;
    private String name;

    public UserObj () {}

    public UserObj (String email, String password, String name) {
        this.email=email;
        this.password=password;
        this.name=name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Step("Generating of user")
    public UserObj getRandomUser() {
        return new UserObj(
                RandomStringUtils.randomAlphanumeric(10)+"@test.com",
                "Password",
                RandomStringUtils.randomAlphabetic(10)
        );
    }
}
