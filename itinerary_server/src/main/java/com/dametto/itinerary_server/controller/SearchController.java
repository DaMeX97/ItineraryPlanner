package com.dametto.itinerary_server.controller;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.entity.TouristAttractionEntity;
import com.dametto.itinerary_server.jpa.Itinerary;
import com.dametto.itinerary_server.jpa.Post;
import com.dametto.itinerary_server.jpa.TouristAttraction;
import com.dametto.itinerary_server.payload.response.BingRouteResponse;
import com.dametto.itinerary_server.service.BingService;
import com.dametto.itinerary_server.service.CityService;
import com.dametto.itinerary_server.service.SearchService;
import com.dametto.itinerary_server.service.TouristAttractionService;
import com.dametto.itinerary_server.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="search")
public class SearchController {
    @Autowired
    SearchService searchService;

    @Autowired
    CityService cityService;

    @Autowired
    TouristAttractionService touristAttractionService;

    @Autowired
    BingService bingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TouristAttraction.class)))),
                    @ApiResponse(responseCode = "404", description = "City not found", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<TouristAttraction> getTouristAttractions(HttpServletResponse response,
                                                        @RequestParam("cityId") Integer cityId,
                                                        @RequestParam("date-start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
                                                        @RequestParam("date-end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd,
                                                        @RequestParam("radiusKm") Optional<Double>  optionalRadiusKm,
                                                        @RequestParam("skip") Optional<Integer> optionalSkip) throws IOException {
        // init parameters
        Double radiusKm = optionalRadiusKm.isEmpty() ? 20 : optionalRadiusKm.get();
        Integer skip = optionalSkip.isEmpty() ? 0 : optionalSkip.get();

        Integer hoursPerDay = 8;
        Integer numberOfDays = DateUtils.getDateDiff(dateStart, dateEnd, TimeUnit.DAYS).intValue() + 1;
        Integer limit = (hoursPerDay*numberOfDays*60) * 2;
        // Value 2 is used because minimum time for attraction is 30 minutes, so we can limit the query
        // in order to optimize it

        // find city
        Optional<CityEntity> cityEntityOptional = cityService.findById(cityId);
        // check if city exist
        if(cityEntityOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "City not found");
            return null;
        }

        CityEntity cityEntity = cityEntityOptional.get();

        // get attractions
        return searchService.getAttractionsNearCity(cityEntity, radiusKm, limit, skip, numberOfDays, hoursPerDay);
    }

    @RequestMapping(value = "suggestions", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TouristAttraction.class)))),
                    @ApiResponse(responseCode = "404", description = "City not found", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<TouristAttraction> getSuggestions(HttpServletResponse response,
                                                        @RequestParam("cityId") Integer cityId,
                                                        @RequestParam("avoid") Optional<String> avoid,
                                                        @RequestParam("limit") Optional<Integer> limitParam,
                                                        @RequestParam("name") Optional<String> name) throws IOException {
        // parameter init
        Integer limit = limitParam.isEmpty() ? 5 : limitParam.get();

        Optional<CityEntity> cityEntity = cityService.findById(cityId);
        // check if city exists
        if(cityEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "City not found");
            return null;
        }

        // create id list to avoid
        List<String> idsStrings = new ArrayList<>();

        if(!avoid.isEmpty()) {
            idsStrings = Arrays.asList(avoid.get().split(","));
        }

        List<Integer> ids = new ArrayList<>();
        for(String id : idsStrings) {
            ids.add(Integer.parseInt(id));
        }

        // return attractions
        return searchService.getSuggestions(cityEntity.get(), ids, limit, name.isEmpty() ? "" : name.get());
    }

    @RequestMapping(value = "routes", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BingRouteResponse.class)))),
                    @ApiResponse(responseCode = "404", description = "Tourist attraction not found", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<BingRouteResponse> getRoutes(HttpServletResponse response,
                                             @RequestParam("startId") Integer startId,
                                             @RequestParam("endId") Integer endId,
                                             @RequestParam("startTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime) throws IOException {

        Optional<TouristAttractionEntity> optionalAttractionStart = touristAttractionService.findById(startId);
        Optional<TouristAttractionEntity> optionalAttractionEnd = touristAttractionService.findById(endId);

        // check if attractions exist
        if(optionalAttractionStart.isEmpty() || optionalAttractionEnd.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tourist attraction not found.");
            return null;
        }

        TouristAttractionEntity attractionStart = optionalAttractionStart.get();
        TouristAttractionEntity attractionEnd = optionalAttractionEnd.get();

        // search route
        return bingService.getRoutes(attractionStart, attractionEnd, startTime, "Driving");
    }
}
