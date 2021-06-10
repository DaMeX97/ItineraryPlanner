package com.dametto.itinerary_server.mapper;

import com.dametto.itinerary_server.entity.FollowEntity;
import com.dametto.itinerary_server.entity.ItineraryEntity;
import com.dametto.itinerary_server.entity.PostEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.jpa.Itinerary;
import com.dametto.itinerary_server.jpa.ShallowUser;
import com.dametto.itinerary_server.jpa.Post;
import com.dametto.itinerary_server.payload.request.RegistrationRequestJpa;
import com.dametto.itinerary_server.jpa.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User entityToJpa(UserEntity ent) {
        User user = new User();
        user.setId(ent.getId());
        user.setEmail(ent.getEmail());
        user.setFirstName(ent.getFirstName());
        user.setLastName(ent.getLastName());

        List<ShallowUser> followRequest = new ArrayList<>();
        List<ShallowUser> followingRequest = new ArrayList<>();

        List<ShallowUser> followers = new ArrayList<>();
        for(FollowEntity entity : ent.getFollowers()) {
            if(entity.getType().equals("FR")) {
                followRequest.add(entityToShallowJpa(entity.getSender()));
            }
            else {
                followers.add(entityToShallowJpa(entity.getSender()));
            }
        }
        user.setFollowRequest(followRequest);
        user.setFollowers(followers);

        List<ShallowUser> following = new ArrayList<>();
        for(FollowEntity entity : ent.getFollowing()) {
            if(entity.getType().equals("FR")) {
                followingRequest.add(entityToShallowJpa(entity.getReceiver()));
            }
            else {
                following.add(entityToShallowJpa(entity.getReceiver()));
            }
        }
        user.setFollowingRequest(followingRequest);
        user.setFollowing(following);

        List<Post> posts = new ArrayList<>();
        for(PostEntity entity : ent.getPosts()) {
            posts.add(PostMapper.entityToJpa(entity));
        }
        user.setPosts(posts);

        List<Itinerary> itineraries = new ArrayList<>();
        for(ItineraryEntity entity : ent.getItineraries()) {
            itineraries.add(ItineraryMapper.entityToJpa(entity));
        }
        user.setItineraries(itineraries);

        return user;
    }

    public static ShallowUser entityToShallowJpa(UserEntity entity) {
        ShallowUser user = new ShallowUser();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setNumberOfFollowers(entity.getFollowers().stream().filter(e -> !e.getType().equals("FR")).collect(Collectors.toList()).size());
        user.setNumberOfFollowing(entity.getFollowing().stream().filter(e -> !e.getType().equals("FR")).collect(Collectors.toList()).size());
        user.setNumberOfPosts(entity.getPosts().size());

        return user;
    }

    public static UserEntity followerToUserEntity(FollowEntity followEntity) {
        return followEntity.getSender();
    }

    public static UserEntity followingToUserEntity(FollowEntity followEntity) {
        return followEntity.getReceiver();
    }

    public static UserEntity jpaToEntity(RegistrationRequestJpa ent) {
        UserEntity user = new UserEntity();
        user.setEmail(ent.getEmail());
        user.setFirstName(ent.getFirstName());
        user.setLastName(ent.getLastName());
        user.setPassword(ent.getPassword());

        return user;
    }

}
