package net.esliceu.Rest_Api_Forum.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class User extends BaseEntity {
    String role;
    String name;
    String email;
    @Column(name = "image_url")
    String imageUrl;
    String password;

    public User(String role, String name, String email, String imageUrl) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
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
}
