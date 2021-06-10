package com.dametto.itinerary_server.payload.request;

public class PostUpdateRequestJpa {
    Integer id;
    String body;

    public PostUpdateRequestJpa() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
