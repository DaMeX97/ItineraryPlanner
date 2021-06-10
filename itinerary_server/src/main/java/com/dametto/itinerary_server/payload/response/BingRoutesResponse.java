package com.dametto.itinerary_server.payload.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BingRoutesResponse {

    private List<BingRouteResource> resourceSets;

    public BingRoutesResponse() {

    }

    public List<BingRouteResource> getResourceSets() {
        return resourceSets;
    }

    public void setResourceSets(List<BingRouteResource> resourceSets) {
        this.resourceSets = resourceSets;
    }
}