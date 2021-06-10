package com.dametto.itinerary_server.mapper;

import com.dametto.itinerary_server.entity.CommentEntity;
import com.dametto.itinerary_server.jpa.Comment;

public class CommentMapper {
    public static Comment entityToJpa(CommentEntity entity) {
        Comment jpa = new Comment();
        jpa.setId(entity.getId());
        jpa.setAuthor(UserMapper.entityToShallowJpa(entity.getAuthor()));
        jpa.setBody(entity.getBody());
        jpa.setCreatedAt(entity.getCreatedAt());

        return jpa;
    }
}
