package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.ItineraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryRepository  extends JpaRepository<ItineraryEntity, Integer> {
    List<ItineraryEntity> findByUserId(Integer userId);
}
