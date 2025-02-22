package net.esliceu.Rest_Api_Forum.Utils;

public class UserToken {
    private UserPermissions user;
    private String token;

    public UserToken(UserPermissions user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserPermissions getUser() {
        return user;
    }

    public void setUser(UserPermissions user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
