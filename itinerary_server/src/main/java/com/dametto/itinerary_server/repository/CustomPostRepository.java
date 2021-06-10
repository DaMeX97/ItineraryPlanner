package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.PostEntity;
import com.dametto.itinerary_server.entity.UserEntity;

import java.awt.print.Pageable;
import java.util.List;

public interface CustomPostRepository {
    List<PostEntity> findByAuthorId(List<UserEntity> userEntities, Integer limit, Integer skip);
}
