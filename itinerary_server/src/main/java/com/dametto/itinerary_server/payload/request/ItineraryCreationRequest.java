package com.dametto.itinerary_server.payload.request;

import com.dametto.itinerary_server.jpa.City;

import java.util.Date;
import java.util.List;

public class ItineraryCreationRequest {
    String name;

    Integer hoursPerDay;

    Integer breakMinutes;

    Date startDate;

    City city;

    List<ItineraryDayRequest> days;

    String status;

    public ItineraryCreationRequest() {

    }

    public Integer getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(Integer hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<ItineraryDayRequest> getDays() {
        return days;
    }

    public void setDays(List<ItineraryDayRequest> days) {
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getBreakMinutes() {
        return breakMinutes;
    }

    public void setBreakMinutes(Integer breakMinutes) {
        this.breakMinutes = breakMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
