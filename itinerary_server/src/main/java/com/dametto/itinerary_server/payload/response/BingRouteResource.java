package com.dametto.itinerary_server.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BingRouteResource {
    private Integer estimatedTotal;
    private List<BingRouteResponse> resources;

    public BingRouteResource() {

    }

    public Integer getEstimatedTotal() {
        return estimatedTotal;
    }

    public void setEstimatedTotal(Integer estimatedTotal) {
        this.estimatedTotal = estimatedTotal;
    }

    public List<BingRouteResponse> getResources() {
        return resources;
    }

    public void setResources(List<BingRouteResponse> resources) {
        this.resources = resources;
    }
}
