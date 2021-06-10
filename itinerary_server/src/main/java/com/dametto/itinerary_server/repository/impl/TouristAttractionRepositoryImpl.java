package com.dametto.itinerary_server.repository.impl;

import com.dametto.itinerary_server.entity.CityEntity;
import com.dametto.itinerary_server.entity.TouristAttractionEntity;
import com.dametto.itinerary_server.repository.CustomTouristAttractionRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class TouristAttractionRepositoryImpl implements CustomTouristAttractionRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TouristAttractionEntity> getAttractionsNearCity(CityEntity cityEntity, Double kmRadius, Integer limit, Integer skip) {
        Double degreesPerKm = 0.009;
        Double totalDegrees = degreesPerKm * kmRadius;
        Double minLat = cityEntity.getLatitude() - totalDegrees;
        Double maxLat = cityEntity.getLatitude() + totalDegrees;
        Double minLong = cityEntity.getLongitude() - (totalDegrees / Math.cos(cityEntity.getLatitude() * Math.PI/180));
        Double maxLong = cityEntity.getLongitude() + (totalDegrees / Math.cos(cityEntity.getLatitude() * Math.PI/180));

        Query query = em.createQuery("" +
                "SELECT ta FROM tourist_attraction ta WHERE " +
                "ta.latitude >= :min_lat AND ta.latitude <= :max_lat AND " +
                "ta.longitude >= :min_lon AND ta.longitude <= :max_lon " +
                "GROUP BY ta " +
                "ORDER BY ta.visits DESC ");

        query.setParameter("min_lat", minLat);
        query.setParameter("max_lat", maxLat);
        query.setParameter("min_lon", minLong);
        query.setParameter("max_lon", maxLong);

        query.setMaxResults(limit);
        query.setFirstResult(skip);

        List<TouristAttractionEntity> results = query.getResultList();

        return results;
    }

    @Override
    public List<TouristAttractionEntity> getSuggestions(CityEntity cityEntity, List<Integer> avoidIds, Integer limit, String name) {
        String queryString = "";
        queryString += "SELECT ta.* FROM tourist_attraction ta ";

        if(avoidIds.size() > 0) {
            queryString += " WHERE ";
        }

        int i = 0;
        for(Integer id : avoidIds) {
            queryString += " ta.id != :id" + i + " ";
            if(i < avoidIds.size() - 1) {
                queryString += " AND ";
            }
            i++;
        }

        if(name.length() > 0) {
            queryString += " AND LOWER(ta.name) LIKE :pattern1 ";
        }

        queryString += " ORDER BY FLOOR((calculate_distance(ta.lat\\:\\:DECIMAL, ta.lon\\:\\:DECIMAL, :latitude\\:\\:DECIMAL, :longitude\\:\\:DECIMAL, 'K'))/(:radius)) ASC, ta.visits DESC LIMIT :limit";

        Query query = em.createNativeQuery(queryString, TouristAttractionEntity.class);
        query.setParameter("longitude", cityEntity.getLongitude());
        query.setParameter("latitude", cityEntity.getLatitude());
        query.setParameter("radius", 5);
        query.setParameter("limit", limit);

        if(name.length() > 0) {
            query.setParameter("pattern1", "%"+name.toLowerCase()+"%");
        }

        i = 0;
        for(Integer id : avoidIds) {
            query.setParameter("id" + i, id);
            i++;
        }

        List<TouristAttractionEntity> results = (List<TouristAttractionEntity>)query.getResultList();

        return results;
    }
}
