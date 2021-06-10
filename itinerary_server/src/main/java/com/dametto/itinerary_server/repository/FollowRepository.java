package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.FollowKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<com.dametto.itinerary_server.entity.FollowEntity, FollowKeyEntity>, CustomFollowRepository {
}
