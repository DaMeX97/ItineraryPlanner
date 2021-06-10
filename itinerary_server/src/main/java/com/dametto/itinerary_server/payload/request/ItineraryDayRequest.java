package com.dametto.itinerary_server.payload.request;

import com.dametto.itinerary_server.jpa.TouristAttraction;

import java.util.List;

public class ItineraryDayRequest {
    List<TouristAttraction> attractions;

    public ItineraryDayRequest() {

    }

    public List<TouristAttraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<TouristAttraction> attractions) {
        this.attractions = attractions;
    }
}