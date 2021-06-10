package com.dametto.itinerary_server.controller;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.jpa.City;
import com.dametto.itinerary_server.jpa.Itinerary;
import com.dametto.itinerary_server.mapper.CityMapper;
import com.dametto.itinerary_server.payload.response.JwtResponse;
import com.dametto.itinerary_server.service.CityService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="city")
public class CityController {

    @Autowired
    CityService cityService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = City.class)))),
                    @ApiResponse(responseCode = "400", description = "Name is mandatory and must be at least 3 characters", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<City> getCities(HttpServletResponse response, @RequestParam("name") String name) throws IOException {
        // check constraints on name
        if(name == null || name.length() < 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name is mandatory and must be at least 3 characters.");
            return null;
        }

        // get cities
        List<CityEntity> cityEntities = cityService.findBySubstringName(name);
        ArrayList<City> cities = new ArrayList<>();

        for(CityEntity city : cityEntities) {
            cities.add(CityMapper.entityToJpa(city));
        }

        return cities;
    }

}
