package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.entity.TouristAttractionEntity;
import com.dametto.itinerary_server.jpa.TouristAttraction;
import com.dametto.itinerary_server.jpa.TravelPath;
import com.dametto.itinerary_server.mapper.TouristAttractionMapper;
import com.dametto.itinerary_server.payload.response.BingRouteResponse;
import com.dametto.itinerary_server.repository.TouristAttractionRepository;
import com.dametto.itinerary_server.utils.DistanceUtils;
import com.dametto.itinerary_server.utils.path.TravelPathDataStr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    TouristAttractionRepository touristAttractionRepository;


    // Create single path
    private TravelPathDataStr createTravelPathDataStr(List<TouristAttraction> array) {
        List<TouristAttraction> attractions = new ArrayList<>(array); // copy of the list

        TravelPathDataStr path = new TravelPathDataStr();
        for(TouristAttraction attraction : attractions) {
            TouristAttraction newNode = attraction;

            path.addTravelNode(newNode);
        }

        return path;
    }

    private TravelPathDataStr simplifyTravelPathDataStr(TravelPathDataStr path, int numberOfDays, int hoursPerDay, List<TouristAttraction> attractions) {
        // Probably the travel path time is exceeding the time available, let's optimize it!
        while(path.getTotalMinutes() > (numberOfDays * hoursPerDay * 60)) {
            // get attraction with minimum visits
            TouristAttraction minumum = null;
            int minumumIndex = 0;
            // Get minimum element
            int i = 0;
            for(TouristAttraction attraction : attractions) {
                if(minumum == null) {
                    minumum = attraction;
                    minumumIndex = i;
                }
                else {
                    if(minumum.getVisits() > attraction.getVisits()) {
                        minumum = attraction;
                        minumumIndex = i;
                    }
                }
                i++;
            }

            TouristAttraction attractionToRemove = attractions.get(minumumIndex);
            attractions.remove(minumumIndex);

            path.removeTravelNode(attractionToRemove);
        }

        return path;
    }


    public List<TouristAttraction> getAttractionsNearCity(CityEntity cityEntity, Double kmRadius, Integer limit, Integer skip, Integer numberOfDays, Integer hoursPerDay) {
        List<TouristAttractionEntity> touristAttractionEntities = touristAttractionRepository.getAttractionsNearCity(cityEntity, kmRadius, limit, skip);

        if(touristAttractionEntities.size() == 0) {
            return new ArrayList<>();
        }

        List<TouristAttraction> attractions = new ArrayList<>();
        for(TouristAttractionEntity entity : touristAttractionEntities) {
            attractions.add(TouristAttractionMapper.entityToJpa(entity));
        }

        TravelPathDataStr path = this.createTravelPathDataStr(attractions);

        path = this.simplifyTravelPathDataStr(path, numberOfDays, hoursPerDay, attractions);

        return path.getTravelPath();
    }

    public List<TouristAttraction> getSuggestions(CityEntity cityEntity, List<Integer> avoidIds, Integer limit, String name) {
        List<TouristAttractionEntity> touristAttractionEntities = touristAttractionRepository.getSuggestions(cityEntity, avoidIds, limit, name);

        List<TouristAttraction> attractions = new ArrayList<>();
        for(TouristAttractionEntity entity : touristAttractionEntities) {
            attractions.add(TouristAttractionMapper.entityToJpa(entity));
        }

        return attractions;
    }
}
