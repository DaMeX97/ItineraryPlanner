package com.dametto.itinerary_server.mapper;

import com.dametto.itinerary_server.entity.TouristAttractionEntity;
import com.dametto.itinerary_server.jpa.TouristAttraction;

import java.util.ArrayList;
import java.util.List;

public class TouristAttractionMapper {
    public static TouristAttraction entityToJpa(TouristAttractionEntity entity) {
        TouristAttraction touristAttraction = new TouristAttraction();
        touristAttraction.setId(entity.getId());
        touristAttraction.setName(entity.getName());
        touristAttraction.setWikidataUrl(entity.getWikidataUrl());
        touristAttraction.setDescription(entity.getDescription());
        touristAttraction.setImageUrl(entity.getImageUrl());
        touristAttraction.setLatitude(entity.getLatitude());
        touristAttraction.setLongitude(entity.getLongitude());
        touristAttraction.setVisits(entity.getVisits());
        touristAttraction.setVisitsDurationMinutes(entity.getVisitsDurationMinutes());
        return touristAttraction;
    }

    public static List<TouristAttraction> entityToJpa(List<TouristAttractionEntity> entities) {
        List<TouristAttraction> jpa = new ArrayList<>();

        for(TouristAttractionEntity attraction : entities) {
            jpa.add(entityToJpa(attraction));
        }

        return jpa;
    }

    public static TouristAttractionEntity jpaToEntity(TouristAttraction entity) {
        TouristAttractionEntity touristAttraction = new TouristAttractionEntity();
        touristAttraction.setId(entity.getId());
        touristAttraction.setName(entity.getName());
        touristAttraction.setWikidataUrl(entity.getWikidataUrl());
        touristAttraction.setDescription(entity.getDescription());
        touristAttraction.setImageUrl(entity.getImageUrl());
        touristAttraction.setLatitude(entity.getLatitude());
        touristAttraction.setLongitude(entity.getLongitude());
        touristAttraction.setVisits(entity.getVisits());
        touristAttraction.setVisitsDurationMinutes(entity.getVisitsDurationMinutes());
        return touristAttraction;
    }

    public static List<TouristAttractionEntity> jpaToEntity(List<TouristAttraction> jpa) {
        List<TouristAttractionEntity> entities = new ArrayList<>();

        for(TouristAttraction attraction : jpa) {
            entities.add(jpaToEntity(attraction));
        }

        return entities;
    }
}
