package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.FollowEntity;
import com.dametto.itinerary_server.entity.FollowKeyEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {
    @Autowired
    FollowRepository followRepository;

    public FollowEntity save(FollowEntity entity) {
        return followRepository.save(entity);
    }

    public Optional<FollowEntity> findById(FollowKeyEntity entity) {
        return followRepository.findById(entity);
    }

    public void deleteById(FollowKeyEntity id) {
        followRepository.deleteById(id);
    }


    public List<FollowEntity> getFollowerRequests(UserEntity entity) {
        return this.followRepository.getFollowerRequests(entity);
    }


    public List<FollowEntity> getFollowing(UserEntity userEntity) {
        return this.followRepository.getFollowing(userEntity);
    }
}
