package com.dametto.itinerary_server.jpa;

public class TouristAttraction {
    private Integer id;

    private String name;

    private String wikidataUrl;

    private String description;

    private String imageUrl;

    private Double latitude;

    private Double longitude;

    private Long visits;

    private Integer visitsDurationMinutes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWikidataUrl() {
        return wikidataUrl;
    }

    public void setWikidataUrl(String wikidataUrl) {
        this.wikidataUrl = wikidataUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

    public Integer getVisitsDurationMinutes() {
        return visitsDurationMinutes;
    }

    public void setVisitsDurationMinutes(Integer visitsDurationMinutes) {
        this.visitsDurationMinutes = visitsDurationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
