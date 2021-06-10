package com.dametto.itinerary_server.repository;

import com.dametto.itinerary_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>, CustomUserRepository {
    UserEntity findByEmail(String email);
}
