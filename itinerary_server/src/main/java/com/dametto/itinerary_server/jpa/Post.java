package com.dametto.itinerary_server.jpa;

import java.util.Date;
import java.util.List;

public class Post {
    private Integer id;

    private ShallowUser author;

    private String body;

    private Date createdAt;

    private List<Comment> comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ShallowUser getAuthor() {
        return author;
    }

    public void setAuthor(ShallowUser author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
