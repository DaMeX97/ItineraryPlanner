package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.PostEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public PostEntity save(PostEntity entity) {
        return postRepository.save(entity);
    }

    public Optional<PostEntity> findById(Integer id) {
        return postRepository.findById(id);
    }

    public List<PostEntity> find(List<UserEntity> userEntities, Integer limit, Integer skip) {
        return postRepository.findByAuthorId(userEntities, limit, skip);
    }

    public List<PostEntity> find(UserEntity userEntity, Integer limit, Integer skip) {
        List<UserEntity> list = new ArrayList();
        list.add(userEntity);
        return this.find(list, limit, skip);
    }

    public void deleteById(Integer id) {
        this.postRepository.deleteById(id);
    }
}
