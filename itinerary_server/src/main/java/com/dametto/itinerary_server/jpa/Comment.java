package com.dametto.itinerary_server.jpa;

import java.util.Date;

public class Comment {
    private Integer id;

    private ShallowUser author;

    private String body;

    private Date createdAt;

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
}
