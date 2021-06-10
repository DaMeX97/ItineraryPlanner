package com.dametto.itinerary_server.controller;

import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.jpa.Itinerary;
import com.dametto.itinerary_server.jpa.ShallowUser;
import com.dametto.itinerary_server.jpa.User;
import com.dametto.itinerary_server.mapper.UserMapper;
import com.dametto.itinerary_server.security.jwt.JwtUtils;
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
@RequestMapping(path="user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShallowUser.class)))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<ShallowUser> searchUsers(HttpServletResponse response, HttpServletRequest request, @RequestParam("q") String query) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        // search
        List<UserEntity> entities = userService.searchUsers(query, userEntity);

        // map and return
        List<ShallowUser> users = new ArrayList<>();
        for(UserEntity entity : entities) {
            // remove current user
            if(!entity.getId().equals(userEntity.getId())) {
                users.add(UserMapper.entityToShallowJpa(entity));
            }
        }

        return users;
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = Void.class)))
            }
    )
    public User getUser(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") Integer userId) throws IOException {
        Optional<UserEntity> userEntity = userService.findById(userId);
        // check if user exist
        if(userEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
            return null;
        }

        // return it
        return UserMapper.entityToJpa(userEntity.get());
    }
}
