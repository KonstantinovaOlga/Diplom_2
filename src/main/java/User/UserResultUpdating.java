package User;

public class UserResultUpdating {
    public boolean success;
    public UserObj user;

    public UserResultUpdating() {}

    public UserResultUpdating(Boolean success, UserObj user) {
        this.success=success;
        this.user=user;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public UserObj getUser() {
        return user;
    }

    public void setUser(UserObj user) {
        this.user = user;
    }

}
