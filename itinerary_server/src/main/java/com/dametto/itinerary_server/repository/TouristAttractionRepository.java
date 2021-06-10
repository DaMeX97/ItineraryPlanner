package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.TouristAttractionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristAttractionRepository extends JpaRepository<TouristAttractionEntity, Integer>, CustomTouristAttractionRepository {
}
