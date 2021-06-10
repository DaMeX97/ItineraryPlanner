package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.CityEntity;

import java.util.List;

public interface CustomCityRepository {
    List<CityEntity> getBySubstringName(String name);
}
