package net.esliceu.Rest_Api_Forum.Utils;

public class UserIat {
    private UserPermissions user;
    private String iat;

    public UserIat(UserPermissions user, String iat) {
        this.user = user;
        this.iat = iat;
    }

    public UserPermissions getUser() {
        return user;
    }

    public void setUser(UserPermissions user) {
        this.user = user;
    }

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }
}
