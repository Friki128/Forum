package net.esliceu.Rest_Api_Forum.Entities;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "images")
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "data", length = 1000000)
    private byte[] imageData;

    @OneToOne
    @JoinColumn(name="user")
    private User user;

    public Image(){}

    public Image(byte[] imageData, User user) {
        this.imageData = imageData;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
