package com.dametto.itinerary_server.jpa;

import java.util.List;

public class ItineraryDay {
    Integer id;

    List<TouristAttraction> attractions;

    public ItineraryDay() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TouristAttraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<TouristAttraction> attractions) {
        this.attractions = attractions;
    }
}
