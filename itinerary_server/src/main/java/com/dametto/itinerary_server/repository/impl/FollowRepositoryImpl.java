package com.dametto.itinerary_server.repository.impl;

import com.dametto.itinerary_server.entity.FollowEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.repository.CustomFollowRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class FollowRepositoryImpl implements CustomFollowRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<FollowEntity> getFollowerRequests(UserEntity userEntity) {
        Query query = em.createQuery("" +
                "SELECT f FROM follow f WHERE f.type = 'FR' AND f.receiver.id = :id", FollowEntity.class);

        query.setParameter("id", userEntity.getId());

        List<FollowEntity> results = query.getResultList();

        return results;
    }

    @Override
    public List<FollowEntity> getFollowing(UserEntity userEntity) {
        Query query = em.createQuery("" +
                "SELECT f FROM follow f WHERE f.type = 'ACCEPTED' AND f.sender.id = :id", FollowEntity.class);

        query.setParameter("id", userEntity.getId());

        List<FollowEntity> results = query.getResultList();

        return results;
    }
}
