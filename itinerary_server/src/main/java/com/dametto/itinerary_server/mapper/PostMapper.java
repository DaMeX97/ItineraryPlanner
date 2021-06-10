package com.dametto.itinerary_server.mapper;

import com.dametto.itinerary_server.entity.CommentEntity;
import com.dametto.itinerary_server.entity.PostEntity;
import com.dametto.itinerary_server.jpa.Comment;
import com.dametto.itinerary_server.jpa.Post;

import java.util.ArrayList;
import java.util.List;

public class PostMapper {
    public static Post entityToJpa(PostEntity entity) {
        Post jpa = new Post();
        jpa.setId(entity.getId());
        jpa.setAuthor(UserMapper.entityToShallowJpa(entity.getAuthor()));
        jpa.setBody(entity.getBody());
        jpa.setCreatedAt(entity.getCreatedAt());

        List<Comment> commentList = new ArrayList<>();
        for(CommentEntity commentEntity : entity.getComments()) {
            commentList.add(CommentMapper.entityToJpa(commentEntity));
        }
        jpa.setComments(commentList);

        return jpa;
    }
}
