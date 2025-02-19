package net.esliceu.Rest_Api_Forum.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Reply extends BaseEntity{
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Topic topic;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private User user;
    private String createdAt;
    private String updatedAt;
}
