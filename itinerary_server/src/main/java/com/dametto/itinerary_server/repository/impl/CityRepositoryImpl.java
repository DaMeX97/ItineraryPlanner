package com.dametto.itinerary_server.repository.impl;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.repository.CustomCityRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CityRepositoryImpl implements CustomCityRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CityEntity> getBySubstringName(String name) {
        Query query = em.createQuery("" +
                "SELECT c FROM city c WHERE LOWER(c.name) LIKE :pattern1 ORDER BY CASE " +
                "WHEN LOWER(c.name) LIKE :pattern2 THEN 1 " +
                "WHEN LOWER(c.name) LIKE :pattern3 THEN 2 " +
                "WHEN LOWER(c.name) LIKE :pattern4 THEN 3 " +
                "else 999 " +
                "END ASC, c.name ASC");

        query.setParameter("pattern1", "%"+name.toLowerCase()+"%");
        query.setParameter("pattern2", name.toLowerCase()+"%");
        query.setParameter("pattern3", "% "+name.toLowerCase()+"%");
        query.setParameter("pattern4", "%'"+name.toLowerCase()+"%");

        List<CityEntity> results = query.getResultList();

        return results;
    }

}
