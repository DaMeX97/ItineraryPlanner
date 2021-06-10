package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.CommentEntity;
import com.dametto.itinerary_server.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public CommentEntity save(CommentEntity entity) {
        return commentRepository.save(entity);
    }

    public Optional<CommentEntity> findById(Integer id) {
        return commentRepository.findById(id);
    }

    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }
}
