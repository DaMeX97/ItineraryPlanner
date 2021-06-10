package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer>, CustomCityRepository {
}
