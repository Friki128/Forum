package net.esliceu.Rest_Api_Forum.Utils;

public class UserIat {
    private String iat;
    private long id;
    private String email;
    private String name;
    private Permissions permissions;
    private String role;
    private String avatarUrl;


    public UserIat(UserPermissions user, String iat) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.permissions = user.getPermissions();
        this.avatarUrl = user.getAvatarUrl();
        this.role = user.getRole();
        this.iat = iat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }
    public long get_id(){return id;}

}
