package com.dametto.itinerary_server.controller;

import com.dametto.itinerary_server.entity.CommentEntity;
import com.dametto.itinerary_server.entity.PostEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.jpa.Comment;
import com.dametto.itinerary_server.mapper.CommentMapper;
import com.dametto.itinerary_server.mapper.PostMapper;
import com.dametto.itinerary_server.payload.request.CommentRequestJpa;
import com.dametto.itinerary_server.payload.request.CommentUpdateRequestJpa;
import com.dametto.itinerary_server.payload.request.PostRequestJpa;
import com.dametto.itinerary_server.security.jwt.JwtUtils;
import com.dametto.itinerary_server.service.CommentService;
import com.dametto.itinerary_server.service.PostService;
import com.dametto.itinerary_server.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="comment")
public class CommentController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @RequestMapping(value = "{postId}", method = RequestMethod.POST)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Comment.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT.", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = Void.class)))
            }
    )
    public Comment createComment(HttpServletResponse response, HttpServletRequest request, @RequestBody CommentRequestJpa commentRequestJpa, @PathVariable(name = "postId") Integer postId) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);

        // check if JWT is valid
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        Optional<PostEntity> optionalPostEntity = postService.findById(postId);
        // check if post exists
        if(optionalPostEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found.");
            return null;
        }

        // create and save comment
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setBody(commentRequestJpa.getBody());
        commentEntity.setPost(optionalPostEntity.get());
        commentEntity.setAuthor(userEntity);

        return CommentMapper.entityToJpa(commentService.save(commentEntity));
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Comment.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Cannot access to this comment.", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = Void.class)))
            }
    )
    public Comment updateComment(HttpServletResponse response, HttpServletRequest request, @RequestBody CommentUpdateRequestJpa commentRequestJpa) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);

        // check if JWT is valid
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return null;
        }

        Optional<CommentEntity> optionalCommentEntity = commentService.findById(commentRequestJpa.getId());
        // check if post exists
        if(optionalCommentEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Comment not found.");
            return null;
        }

        CommentEntity entity = optionalCommentEntity.get();

        if(!entity.getAuthor().getId().equals(userEntity.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot access to this comment.");
            return null;
        }

        entity.setBody(commentRequestJpa.getBody());

        return CommentMapper.entityToJpa(commentService.save(entity));
    }

    @RequestMapping(value = "{commentId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid JWT/Cannot delete this comment", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Cannot access to this comment.", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = Void.class)))
            }
    )
    public void deleteComment(HttpServletResponse response, HttpServletRequest request, @PathVariable(name = "commentId") Integer commentId) throws IOException {
        String email = jwtUtils.getEmailFromHeader(request);
        UserEntity userEntity = userService.findByEmail(email);

        // check if JWT is valid
        if(userEntity == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT.");
            return;
        }

        Optional<CommentEntity> optionalCommentEntity = commentService.findById(commentId);
        // check if post exists
        if(optionalCommentEntity.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Comment not found.");
            return;
        }

        CommentEntity entity = optionalCommentEntity.get();

        if(!entity.getAuthor().getId().equals(userEntity.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot access to this comment.");
            return;
        }

        commentService.deleteById(commentId);
    }
}
