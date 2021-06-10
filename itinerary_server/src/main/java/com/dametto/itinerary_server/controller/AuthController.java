package com.dametto.itinerary_server.controller;

import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.jpa.ShallowUser;
import com.dametto.itinerary_server.payload.request.LoginRequestJpa;
import com.dametto.itinerary_server.payload.request.RegistrationRequestJpa;
import com.dametto.itinerary_server.mapper.UserMapper;
import com.dametto.itinerary_server.payload.response.JwtResponse;
import com.dametto.itinerary_server.security.jwt.JwtUtils;
import com.dametto.itinerary_server.service.UserService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public JwtResponse login(@RequestBody LoginRequestJpa body, HttpServletResponse response) throws IOException {
        // get user
        UserEntity entity = userService.findByEmail(body.getEmail());

        // check if user exists, otherwise return 404
        if(entity == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
            return null;
        }

        return generateJwt(entity, body.getPassword());
    }

    public JwtResponse generateJwt(UserEntity entity, String password) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(entity.getEmail(), password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // return JWT object
        UserEntity userDetails = (UserEntity) authentication.getPrincipal();
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                jwtUtils.getExpirationDate(new Date()));
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "400", description = "The confirm password is not equal to the password", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public JwtResponse register(@RequestBody RegistrationRequestJpa body, HttpServletResponse response) throws IOException {
        if(!body.getPassword().equals(body.getConfirmPassword())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The confirm password is not equal to the password.");
            return null;
        }
        /*else if(!Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).{8,}$").matcher(body.getPassword()).find()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password must have at least one upper case letter and one lower case letter.");
            return null;
        }*/

        // Map jpa to Entity
        UserEntity userEntity = UserMapper.jpaToEntity(body);

        // control if user already exists
        UserEntity anotherUser = userService.findByEmail(userEntity.getEmail());

        if(anotherUser == null) {
            // if not exists then register user
            userEntity.setPassword(encoder.encode(userEntity.getPassword()));

            userEntity = userService.saveUser(userEntity);

            return generateJwt(userEntity, body.getPassword());
        }
        else {
            // if exists return null
            response.sendError(HttpServletResponse.SC_CONFLICT, "User already exists.");
            return null;
        }
    }

    @RequestMapping(value = "refreshToken", method = RequestMethod.GET)
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User not found", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getHeader("Authorization").split(" ")[1];
        String email = jwtUtils.getEmailFromJwtToken(token);

        UserEntity userEntity = userService.findByEmail(email);

        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found.");
            return null;
        }

        // refresh token and return new one

        String newToken = jwtUtils.doGenerateRefreshToken(email);

        return new JwtResponse(newToken,
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                jwtUtils.getExpirationDate(new Date()));
    }
}
