package net.esliceu.Rest_Api_Forum.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Category extends BaseEntity{
    private String title;
    private String description;
    private String slug;
    private String color;
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "moderator_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private List<User> moderators;

    public Category(String title, String description, String slug, String color, List<User> moderators) {
        this.title = title;
        this.description = description;
        this.slug = slug;
        this.color = color;
        this.moderators = moderators;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<User> getModerators() {
        return moderators;
    }

    public void setModerators(List<User> moderators) {
        this.moderators = moderators;
    }
}
