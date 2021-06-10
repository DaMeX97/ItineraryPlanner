package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    @Autowired
    CityRepository cityRepository;

    public List<CityEntity> findBySubstringName(String name) {
        return cityRepository.getBySubstringName(name);
    }

    public Optional<CityEntity> findById(Integer id) {
        return cityRepository.findById(id);
    }
}
