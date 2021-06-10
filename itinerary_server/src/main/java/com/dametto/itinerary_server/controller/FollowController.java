package com.dametto.itinerary_server.controller;

import com.dametto.itinerary_server.entity.FollowEntity;
import com.dametto.itinerary_server.entity.FollowKeyEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.jpa.City;
import com.dametto.itinerary_server.jpa.Itinerary;
import com.dametto.itinerary_server.jpa.ShallowUser;
import com.dametto.itinerary_server.mapper.UserMapper;
import com.dametto.itinerary_server.security.jwt.JwtUtils;
import com.dametto.itinerary_server.service.FollowService;
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
@RequestMapping(path="follow")
public class FollowController {
    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    JwtUtils jwtUtils;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShallowUser.class)))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<ShallowUser> getFollowerRequests(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);

        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        // get FR
        List<FollowEntity> entities = followService.getFollowerRequests(userEntity);

        // map and return
        List<ShallowUser> users = new ArrayList<>();
        for(FollowEntity entity : entities) {
            users.add(UserMapper.entityToShallowJpa(entity.getSender()));
        }

        return users;
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.POST)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "Follow Entity already exists", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public void sendFollowRequest(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") Integer id) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);

        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return;
        }

        Optional<UserEntity> followUser = userService.findById(id);
        // check if user exist
        if(followUser.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // create key
        FollowKeyEntity key = new FollowKeyEntity();
        key.setSenderId(userEntity.getId());
        key.setReceiverId(id);

        // search if already exist
        Optional<FollowEntity> existingFE = followService.findById(key);
        if(existingFE.isPresent()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "A Follow Entity already exists.");
            return;
        }

        // create and save followEntity
        FollowEntity followEntity = new FollowEntity();
        followEntity.setId(key);
        followEntity.setSender(userEntity);
        followEntity.setReceiver(followUser.get());
        followEntity.setType("FR");

        followService.save(followEntity);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.PUT)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Follow reqeust not found", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public void acceptFollowRequest(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") Integer id) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);

        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return;
        }

        Optional<UserEntity> followUser = userService.findById(id);
        // check if user exist
        if(followUser.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Following user not found.");
            return;
        }

        FollowKeyEntity key = new FollowKeyEntity();
        key.setReceiverId(userEntity.getId());
        key.setSenderId(id);

        Optional<FollowEntity> entity = followService.findById(key);
        // check if FR exist
        if(entity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No following request to accept/reject..");
            return;
        }
        else if(!entity.get().getType().equals("FR")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No following request to accept/reject..");
            return;
        }

        // update
        FollowEntity followEntity = entity.get();
        followEntity.setType("ACCEPTED");

        followService.save(followEntity);
    }

    @RequestMapping(value = "{requestId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Follow reqeust not found", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public void denyFollowRequest(HttpServletResponse response, HttpServletRequest request, @PathVariable("requestId") Integer id) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return;
        }

        Optional<UserEntity> followUser = userService.findById(id);
        // check if user exists
        if(followUser.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Following user not found.");
            return;
        }

        FollowKeyEntity key = new FollowKeyEntity();
        key.setReceiverId(userEntity.getId());
        key.setSenderId(id);

        Optional<FollowEntity> entity = followService.findById(key);
        // check if FR exists
        if(entity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No following request to accept/reject.");
            return;
        }
        else if(!entity.get().getType().equals("FR")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No following request to accept/reject.");
            return;
        }

        // delete FR
        followService.deleteById(key);
    }

}
