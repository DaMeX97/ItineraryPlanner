package com.dametto.itinerary_server.controller;

import com.dametto.itinerary_server.entity.CommentEntity;
import com.dametto.itinerary_server.entity.FollowEntity;
import com.dametto.itinerary_server.entity.PostEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.jpa.Comment;
import com.dametto.itinerary_server.jpa.Itinerary;
import com.dametto.itinerary_server.jpa.Post;
import com.dametto.itinerary_server.jpa.User;
import com.dametto.itinerary_server.mapper.PostMapper;
import com.dametto.itinerary_server.mapper.UserMapper;
import com.dametto.itinerary_server.payload.request.PostRequestJpa;
import com.dametto.itinerary_server.payload.request.PostUpdateRequestJpa;
import com.dametto.itinerary_server.security.jwt.JwtUtils;
import com.dametto.itinerary_server.service.FollowService;
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
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="post")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    JwtUtils jwtUtils;

    @RequestMapping(value = "posts", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class)))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<Post> getPosts(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        // get my following user
        List<FollowEntity> users = followService.getFollowing(userEntity);
        List<UserEntity> userEntities = users.stream().map(e -> UserMapper.followingToUserEntity(e)).collect(Collectors.toList());

        // get also my posts
        userEntities.add(userEntity);

        // get posts
        List<PostEntity> postEntities = postService.find(userEntities, 10, 0);

        // map and return
        List<Post> posts = new ArrayList<>();
        for(PostEntity postEntity : postEntities) {
            posts.add(PostMapper.entityToJpa(postEntity));
        }

        return posts;
    }

    @RequestMapping(value = "posts/{userId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class)))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public List<Post> getUserPosts(HttpServletResponse response, HttpServletRequest request, @PathVariable(name = "userId") Integer userId) throws IOException {
        Optional<UserEntity> optionalUserEntity = userService.findById(userId);
        // check if user exists
        if(optionalUserEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
            return null;
        }

        UserEntity userEntity = optionalUserEntity.get();

        // get user post
        List<PostEntity> postEntities = postService.find(userEntity, 10, 0);

        // map and return
        List<Post> posts = new ArrayList<>();
        for(PostEntity postEntity : postEntities) {
            posts.add(PostMapper.entityToJpa(postEntity));
        }

        return posts;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class)))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public Post createPost(HttpServletResponse response, HttpServletRequest request, @RequestBody PostRequestJpa postRequestJpa) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        // create and save post
        PostEntity postEntity = new PostEntity();
        postEntity.setBody(postRequestJpa.getBody());
        postEntity.setAuthor(userEntity);

        return PostMapper.entityToJpa(postService.save(postEntity));
    }


    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Post.class)))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Can't access to this post", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public Post updatePost(HttpServletResponse response, HttpServletRequest request, @RequestBody PostUpdateRequestJpa postRequestJpa) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        Optional<PostEntity> optionalPostEntity = postService.findById(postRequestJpa.getId());

        if(optionalPostEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
            return null;
        }

        // upadte save post
        PostEntity postEntity = optionalPostEntity.get();

        if(!postEntity.getAuthor().getId().equals(userEntity.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Can't access to this post.");
            return null;
        }

        postEntity.setBody(postRequestJpa.getBody());

        return PostMapper.entityToJpa(postService.save(postEntity));
    }

    @RequestMapping(value = "{postId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Can't access to this post", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = Void.class))),
            }
    )
    public void deletePost(HttpServletResponse response, HttpServletRequest request, @PathVariable(name = "postId") Integer postId) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);
        // check JWT
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return;
        }

        Optional<PostEntity> optionalPostEntity = postService.findById(postId);

        if(optionalPostEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
            return;
        }

        // upadte save post
        PostEntity postEntity = optionalPostEntity.get();

        if(!postEntity.getAuthor().getId().equals(userEntity.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Can't access to this post.");
            return;
        }

        postService.deleteById(postId);
    }
}
