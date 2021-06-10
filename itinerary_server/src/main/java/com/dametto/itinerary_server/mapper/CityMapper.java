package com.dametto.itinerary_server.mapper;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.jpa.City;

public class CityMapper {
    public static City entityToJpa(CityEntity entity) {
        City city = new City();
        city.setId(entity.getId());
        city.setName(entity.getName());
        city.setLatitude(entity.getLatitude());
        city.setLongitude(entity.getLongitude());
        city.setProvinceCode(entity.getProvinceCode());

        return city;
    }

    public static CityEntity jpaToEntity(City jpa) {
        CityEntity entity = new CityEntity();
        entity.setId(jpa.getId());
        entity.setName(jpa.getName());
        entity.setLatitude(jpa.getLatitude());
        entity.setLongitude(jpa.getLongitude());
        entity.setProvinceCode(jpa.getProvinceCode());

        return entity;
    }
}
