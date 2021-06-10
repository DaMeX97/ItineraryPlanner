package com.dametto.itinerary_server.jpa;

import java.util.List;

public class User {
    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private List<ShallowUser> followers;

    private List<ShallowUser> following;

    private List<ShallowUser> followRequest;

    private List<ShallowUser> followingRequest;

    private List<Post> posts;

    private List<Itinerary> itineraries;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<ShallowUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<ShallowUser> followers) {
        this.followers = followers;
    }

    public List<ShallowUser> getFollowing() {
        return following;
    }

    public void setFollowing(List<ShallowUser> following) {
        this.following = following;
    }

    public List<ShallowUser> getFollowRequest() {
        return followRequest;
    }

    public void setFollowRequest(List<ShallowUser> followRequest) {
        this.followRequest = followRequest;
    }

    public List<ShallowUser> getFollowingRequest() {
        return followingRequest;
    }

    public void setFollowingRequest(List<ShallowUser> followingRequest) {
        this.followingRequest = followingRequest;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Itinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }
}
