package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer>, CustomPostRepository {

}
