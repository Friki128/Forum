package net.esliceu.Rest_Api_Forum.Utils;

import net.esliceu.Rest_Api_Forum.Entities.Category;
import net.esliceu.Rest_Api_Forum.Entities.User;

import java.util.List;

public class UserPermissions {
    private long id;
    private String email;
    private String name;
    private String role;
    private String avatarUrl;
    private Permissions permissions;

    public UserPermissions(User user, List<Category> categories) {
        this.id = user.getId();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.avatarUrl = user.getAvatarUrl();
        this.name = user.getName();
        this.permissions = new Permissions(user.getRole().equals("admin"), categories);
    }

    public UserPermissions(User user) {
        this.id = user.getId();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.avatarUrl = user.getAvatarUrl();
        this.name = user.getName();
        this.permissions = null;
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

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
    public long get_id(){return id;}
}
