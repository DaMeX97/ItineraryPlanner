package com.dametto.itinerary_server.utils.path;

class TravelEdgeDataStr {
    private TravelNodeDataStr first = null, second = null;

    public TravelEdgeDataStr(TravelNodeDataStr first, TravelNodeDataStr second) {
        this.first = first;
        this.second = second;
    }

    public TravelNodeDataStr getFirst() {
        return first;
    }

    public TravelNodeDataStr getSecond() {
        return second;
    }
}
