package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.TouristAttractionEntity;
import com.dametto.itinerary_server.jpa.TouristAttraction;
import com.dametto.itinerary_server.repository.TouristAttractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TouristAttractionService {

    @Autowired
    private TouristAttractionRepository touristAttractionRepository;

    public Optional<TouristAttractionEntity> findById(Integer id) {
        return touristAttractionRepository.findById(id);
    }

}
