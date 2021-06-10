package com.dametto.itinerary_server.repository.impl;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.entity.FollowEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.repository.CustomUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class UserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserEntity> getBySubstringName(String name) {
        Query query = em.createQuery("" +
                "SELECT u FROM user u WHERE LOWER(u.firstName) LIKE :pattern1 OR LOWER(u.lastName) LIKE :pattern1 OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE :pattern1 OR LOWER(u.lastName) LIKE :pattern1", UserEntity.class);

        query.setParameter("pattern1", "%"+name.toLowerCase()+"%");

        List<UserEntity> results = query.getResultList();

        return results;
    }
}
