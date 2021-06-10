package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.FollowEntity;
import com.dametto.itinerary_server.entity.UserEntity;

import java.util.List;

public interface CustomUserRepository {
    List<UserEntity> getBySubstringName(String name);
}
