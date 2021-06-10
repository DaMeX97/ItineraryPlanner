package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.entity.TouristAttractionEntity;

import java.util.List;

public interface CustomTouristAttractionRepository {
    List<TouristAttractionEntity> getAttractionsNearCity(CityEntity cityEntity, Double kmRadius, Integer limit, Integer skip);

    List<TouristAttractionEntity> getSuggestions(CityEntity cityEntity, List<Integer> avoidIds, Integer limit, String name);
}
