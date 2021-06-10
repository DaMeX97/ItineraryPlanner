package com.dametto.itinerary_server.controller;

import com.dametto.itinerary_server.entity.*;
import com.dametto.itinerary_server.jpa.Itinerary;
import com.dametto.itinerary_server.jpa.TouristAttraction;
import com.dametto.itinerary_server.mapper.ItineraryMapper;
import com.dametto.itinerary_server.mapper.TouristAttractionMapper;
import com.dametto.itinerary_server.payload.request.ItineraryCreationRequest;
import com.dametto.itinerary_server.payload.request.ItineraryUpdateRequest;
import com.dametto.itinerary_server.security.jwt.JwtUtils;
import com.dametto.itinerary_server.service.CityService;
import com.dametto.itinerary_server.service.ItineraryService;
import com.dametto.itinerary_server.service.PostService;
import com.dametto.itinerary_server.service.UserService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="itinerary")
public class ItineraryController {
    @Autowired
    UserService userService;

    @Autowired
    ItineraryService itineraryService;

    @Autowired
    PostService postService;

    @Autowired
    CityService cityService;

    @Autowired
    JwtUtils jwtUtils;

    @RequestMapping(value = "{itineraryId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Itinerary.class))),
                    @ApiResponse(responseCode = "404", description = "Itinerary not found", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Can't access to this itinerary", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public Itinerary getItinerary(HttpServletResponse response, HttpServletRequest request, @PathVariable("itineraryId") Integer id) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);

        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        Optional<ItineraryEntity> optional = itineraryService.findById(id);
        // check itinerary exist
        if(optional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Itinerary not found.");
            return null;
        }

        ItineraryEntity entity = optional.get();
        // check if can get itinerary
        if(!entity.getUser().getId().equals(userEntity.getId()) && !entity.getPublicVisibility()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Can't access to this itinerary");
            return null;
        }

        // return
        return ItineraryMapper.entityToJpa(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Itinerary.class))),
                    @ApiResponse(responseCode = "404", description = "City not found", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public Itinerary createItinerary(@RequestBody ItineraryCreationRequest body, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        // check if city exists
        Optional<CityEntity> optionalCityEntity = cityService.findById(body.getCity().getId());
        if(optionalCityEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "City not found.");
            return null;
        }

        // create and save
        ItineraryEntity entity = ItineraryMapper.requestToEntity(body);
        entity.setUser(userEntity);
        entity.setCity(optionalCityEntity.get());


        entity = itineraryService.saveItinerary(entity);

        // return
        return ItineraryMapper.entityToJpa(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Itinerary.class))),
                    @ApiResponse(responseCode = "404", description = "Itinerary not found", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Can't access to this itinerary", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public Itinerary updateItinerary(@RequestBody ItineraryUpdateRequest body, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        // check if itinerary exists
        Optional<ItineraryEntity> optional = itineraryService.findById(body.getId());

        if(optional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Itinerary not found.");
            return null;
        }

        ItineraryEntity entityToSave = optional.get();
        // check if can modify it
        if(!entityToSave.getUser().getId().equals(userEntity.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Can't access to this itinerary");
            return null;
        }

        // update fields
        entityToSave.setBreakMinutes(body.getBreakMinutes());
        entityToSave.setHoursPerDay(body.getHoursPerDay());
        entityToSave.setName(body.getName());

        int i = 0;
        for(ItineraryDayEntity day : entityToSave.getDays()) {
            List<TouristAttractionEntity> entities = new ArrayList<>();

            for(TouristAttraction attraction : body.getDays().get(i).getAttractions()) {
                entities.add(TouristAttractionMapper.jpaToEntity(attraction));
            }

            day.setAttractions(entities);
            i++;
        }

        // update DB
        entityToSave = itineraryService.updateItinerary(entityToSave);

        // return
        return ItineraryMapper.entityToJpa(entityToSave);
    }

    @RequestMapping(value = "{itineraryId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Itinerary not found", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Can't access to this itinerary", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public void deleteItinerary(HttpServletResponse response, HttpServletRequest request, @PathVariable("itineraryId") Integer id) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return;
        }

        // check if itinerary exist
        Optional<ItineraryEntity> optional = itineraryService.findById(id);

        if(optional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Itinerary not found.");
            return;
        }

        // check if can access to itinerary
        ItineraryEntity entity = optional.get();

        if(!entity.getUser().getId().equals(userEntity.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Can't access to this itinerary");
            return;
        }

        // delete
        itineraryService.deleteItinerary(entity);
    }

    @RequestMapping(value = "itineraries", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Itinerary.class)))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<Itinerary> getItineraries(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        // get and return list
        List<ItineraryEntity> itineraries = itineraryService.getItineraries(userEntity);

        return ItineraryMapper.itineraryListEntityToJpa(itineraries);
    }

    @RequestMapping(value = "{itineraryId}/confirm", method = RequestMethod.PUT)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Itinerary.class))),
                    @ApiResponse(responseCode = "404", description = "Itinerary not found", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Can't access to this itinerary", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public Itinerary confirmItinerary(HttpServletResponse response, HttpServletRequest request, @PathVariable("itineraryId") Integer id) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        // find and check itinerary if exist
        Optional<ItineraryEntity> optional = itineraryService.findById(id);

        if(optional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Itinerary not found.");
            return null;
        }

        // check if can edit
        ItineraryEntity entityToSave = optional.get();

        if(!entityToSave.getUser().getId().equals(userEntity.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Can't access to this itinerary");
            return null;
        }

        // update
        entityToSave.setStatus("VISITED");
        entityToSave.setPublicVisibility(true);

        entityToSave = itineraryService.updateItinerary(entityToSave);

        // create social post
        PostEntity postEntity = new PostEntity();
        postEntity.setAuthor(userEntity);
        postEntity.setBody("I just planned a trip to " + entityToSave.getCity().getName() + "!");

        // save post
        postService.save(postEntity);

        // return
        return ItineraryMapper.entityToJpa(entityToSave);
    }
}
