package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.TouristAttractionEntity;
import com.dametto.itinerary_server.jpa.TouristAttraction;
import com.dametto.itinerary_server.mapper.TouristAttractionMapper;
import com.dametto.itinerary_server.payload.response.BingRouteResponse;
import com.dametto.itinerary_server.payload.response.BingRoutesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class BingService {

    @Value("${bingToken}")
    private String API_KEY;

    private final static String BING_ROUTES_URI = "http://dev.virtualearth.net/REST/v1/Routes/";

    @Autowired
    private RestTemplate restTemplate;

    public List<BingRouteResponse> getRoutes(TouristAttractionEntity start, TouristAttractionEntity end, LocalTime startingTime, String travelMode) {
        return this.getRoutes(TouristAttractionMapper.entityToJpa(start), TouristAttractionMapper.entityToJpa(end), startingTime, travelMode);
    }

    public List<BingRouteResponse> getRoutes(TouristAttractionEntity start, TouristAttractionEntity end, LocalTime startingTime) {
        return this.getRoutes(TouristAttractionMapper.entityToJpa(start), TouristAttractionMapper.entityToJpa(end), startingTime, "Transit");
    }

    public List<BingRouteResponse> getRoutes(TouristAttraction start, TouristAttraction end, LocalTime startingTime) {
        return this.getRoutes(start, end, startingTime, "Transit");
    }

    public List<BingRouteResponse> getRoutes(TouristAttraction start, TouristAttraction end, LocalTime startingTime, String travelMode) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        String apiUrl = BING_ROUTES_URI + "%s?wp.0=%s,%s&wp.1=%s,%s&timeType=Departure&dateTime=%s&key=" + API_KEY;

        apiUrl = String.format(apiUrl, travelMode, start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude(), startingTime.format(dtf));

        ResponseEntity<BingRoutesResponse> response = restTemplate.getForEntity(apiUrl, BingRoutesResponse.class);

        List<BingRouteResponse> result = new ArrayList<>();

        if(response.hasBody()) {
            BingRoutesResponse body = response.getBody();
            if(body.getResourceSets().size() > 0) {
                result = body.getResourceSets().get(0).getResources();
            }
        }

        return result;
    }
}
