package com.dametto.itinerary_server.utils.path;

import com.dametto.itinerary_server.jpa.TouristAttraction;

class TravelNodeDataStr {
    private TravelEdgeDataStr predEdge = null, succEdge = null;
    private TouristAttraction touristAttraction = null;

    public TravelNodeDataStr(TouristAttraction touristAttraction) {
        this.touristAttraction = touristAttraction;
        this.predEdge = null;
        this.succEdge = null;
    }

    public TouristAttraction getTouristAttraction() {
        return touristAttraction;
    }

    public void addSuccessor(TravelNodeDataStr successor) {
        this.succEdge = new TravelEdgeDataStr(this, successor);
    }

    public void addPredecessor(TravelNodeDataStr predecessor) {
        this.predEdge = new TravelEdgeDataStr(predecessor, this);
    }

    public TravelEdgeDataStr getPredEdge() {
        return predEdge;
    }

    public TravelEdgeDataStr getSuccEdge() {
        return succEdge;
    }

    public void setPredEdge(TravelEdgeDataStr predEdge) {
        this.predEdge = predEdge;
    }

    public void setSuccEdge(TravelEdgeDataStr succEdge) {
        this.succEdge = succEdge;
    }
}
