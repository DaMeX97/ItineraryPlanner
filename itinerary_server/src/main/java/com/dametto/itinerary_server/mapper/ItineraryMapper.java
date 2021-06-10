package com.dametto.itinerary_server.mapper;

import com.dametto.itinerary_server.entity.ItineraryDayEntity;
import com.dametto.itinerary_server.entity.ItineraryEntity;
import com.dametto.itinerary_server.jpa.Itinerary;
import com.dametto.itinerary_server.jpa.ItineraryDay;
import com.dametto.itinerary_server.payload.request.ItineraryCreationRequest;
import com.dametto.itinerary_server.payload.request.ItineraryDayRequest;
import com.dametto.itinerary_server.payload.request.ItineraryUpdateRequest;

import java.util.ArrayList;
import java.util.List;

public class ItineraryMapper {
    public static List<Itinerary> itineraryListEntityToJpa(List<ItineraryEntity> entities) {
        List<Itinerary> jpa = new ArrayList<>();

        for(ItineraryEntity entity : entities) {
            jpa.add(entityToJpa(entity));
        }

        return jpa;
    }

    public static Itinerary entityToJpa(ItineraryEntity entity) {
        Itinerary jpa = new Itinerary();

        jpa.setId(entity.getId());
        jpa.setStartDate(entity.getStartDay());
        jpa.setStatus(entity.getStatus());
        jpa.setHoursPerDay(entity.getHoursPerDay());
        jpa.setDays(entityToJpa(entity.getDays()));
        jpa.setUser(UserMapper.entityToShallowJpa(entity.getUser()));
        jpa.setName(entity.getName());
        jpa.setCity(CityMapper.entityToJpa(entity.getCity()));
        jpa.setBreakMinutes(entity.getBreakMinutes());

        return jpa;
    }

    public static ItineraryDay entityToJpa(ItineraryDayEntity entity) {
        ItineraryDay jpa = new ItineraryDay();
        jpa.setId(entity.getId());
        jpa.setAttractions(TouristAttractionMapper.entityToJpa(entity.getAttractions()));

        return jpa;
    }

    public static List<ItineraryDay> entityToJpa(List<ItineraryDayEntity> entities) {
        List<ItineraryDay> jpa = new ArrayList<>();

        for(ItineraryDayEntity attraction : entities) {
            jpa.add(entityToJpa(attraction));
        }

        return jpa;
    }

    public static ItineraryEntity requestToEntity(ItineraryCreationRequest request) {
        ItineraryEntity entity = new ItineraryEntity();
        entity.setHoursPerDay(request.getHoursPerDay());
        entity.setStartDay(request.getStartDate());
        entity.setDays(requestToEntity(request.getDays(), entity));
        entity.setStatus("DRAFT");
        entity.setName(request.getName());
        entity.setBreakMinutes(request.getBreakMinutes());
        entity.setCity(CityMapper.jpaToEntity(request.getCity()));

        return entity;
    }

    public static ItineraryEntity requestToEntity(ItineraryUpdateRequest request) {
        ItineraryEntity entity = new ItineraryEntity();
        entity.setId(request.getId());
        entity.setHoursPerDay(request.getHoursPerDay());
        entity.setStartDay(request.getStartDate());
        entity.setDays(requestToEntity(request.getDays(), entity));
        entity.setStatus("DRAFT");
        entity.setName(request.getName());
        entity.setBreakMinutes(request.getBreakMinutes());
        entity.setCity(CityMapper.jpaToEntity(request.getCity()));

        return entity;
    }

    public static ItineraryDayEntity requestToEntity(ItineraryDayRequest request) {
        ItineraryDayEntity entity = new ItineraryDayEntity();
        entity.setAttractions(TouristAttractionMapper.jpaToEntity(request.getAttractions()));
        return entity;
    }

    public static List<ItineraryDayEntity> requestToEntity(List<ItineraryDayRequest> requests, ItineraryEntity parent) {
        List<ItineraryDayEntity> entities = new ArrayList<>();
        for(ItineraryDayRequest day : requests) {
            ItineraryDayEntity entity = requestToEntity(day);
            entity.setItinerary(parent);
            entities.add(entity);
        }
        return entities;
    }
}
