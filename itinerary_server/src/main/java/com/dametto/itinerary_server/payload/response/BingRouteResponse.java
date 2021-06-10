package com.dametto.itinerary_server.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BingRouteResponse {

    private Long travelDuration;
    private String durationUnit;

    private String travelMode;

    private String distanceUnit;

    public BingRouteResponse() {

    }

    public Long getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(Long travelDuration) {
        this.travelDuration = travelDuration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public Float getDurationInMinutes() {
        if(durationUnit.equals("Second")) {
            return travelDuration.floatValue() / 60f;
        }
        else return travelDuration.floatValue();
    }
}
