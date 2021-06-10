package com.dametto.itinerary_server.repository.impl;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.entity.PostEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.repository.CustomPostRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class PostRepositoryImpl implements CustomPostRepository {
    @PersistenceContext
    private EntityManager em;


    @Override
    public List<PostEntity> findByAuthorId(List<UserEntity> userEntities, Integer limit, Integer skip) {
        Query query = em.createQuery("" +
                "SELECT p FROM post p WHERE p.author in :ids ORDER BY p.createdAt DESC");

        query.setParameter("ids", userEntities);
        query.setMaxResults(limit);
        query.setFirstResult(skip);

        List<PostEntity> results = query.getResultList();

        return results;
    }
}
