package com.dametto.itinerary_server.jpa;

import java.util.ArrayList;

public class TravelPath {
    public ArrayList<TouristAttraction> nodes;

    public TravelPath() {
        this.nodes = new ArrayList<>();
    }

    public void addNewNode(TouristAttraction attraction) {
        this.nodes.add(attraction);
    }
}
