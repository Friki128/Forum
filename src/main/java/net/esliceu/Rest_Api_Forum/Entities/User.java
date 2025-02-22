package net.esliceu.Rest_Api_Forum.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class User extends BaseEntity {
    private String role;
    private String name;
    private String email;
    @Column(name = "image_url")
    private String imageUrl;
    private String password;

    public User(){}

    public User(String role, String name, String email, String imageUrl, String password) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
