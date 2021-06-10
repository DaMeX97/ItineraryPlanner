package com.dametto.itinerary_server.payload.request;

import com.dametto.itinerary_server.jpa.City;
import com.dametto.itinerary_server.jpa.ItineraryDay;

import java.util.Date;
import java.util.List;

public class ItineraryUpdateRequest {
    Integer id;

    String name;

    Integer hoursPerDay;

    Integer breakMinutes;

    Date startDate;

    City city;

    List<ItineraryDayRequest> days;

    String status;

    public ItineraryUpdateRequest() {

    }

    public Integer getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(Integer hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBreakMinutes() {
        return breakMinutes;
    }

    public void setBreakMinutes(Integer breakMinutes) {
        this.breakMinutes = breakMinutes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
