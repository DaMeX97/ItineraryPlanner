package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.ItineraryEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItineraryService {
    @Autowired
    ItineraryRepository itineraryRepository;

    public ItineraryEntity saveItinerary(ItineraryEntity itineraryEntity) {
        return this.itineraryRepository.save(itineraryEntity);
    }

    public ItineraryEntity updateItinerary(ItineraryEntity itineraryEntity) {
        return this.itineraryRepository.save(itineraryEntity);
    }

    public void deleteItinerary(ItineraryEntity itineraryEntity) {
        this.itineraryRepository.deleteById(itineraryEntity.getId());
    }

    public Optional<ItineraryEntity> findById(Integer id) {
        return this.itineraryRepository.findById(id);
    }

    public List<ItineraryEntity> getItineraries(UserEntity entity) {
        return this.itineraryRepository.findByUserId(entity.getId());
    }
}
