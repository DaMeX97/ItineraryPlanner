package com.dametto.itinerary_server.service;

import com.dametto.itinerary_server.entity.FollowEntity;
import com.dametto.itinerary_server.entity.UserEntity;
import com.dametto.itinerary_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    public Optional<UserEntity> findById(Integer id) {
        return userRepository.findById(id);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity saveUser(UserEntity ent) {
        return userRepository.save(ent);
    }

    @Transactional
    public UserEntity loadUserByEmail(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.loadUserByEmail(email);
    }

    public List<UserEntity> searchUsers(String name, UserEntity userEntity) {
        List<UserEntity> entities = userRepository.getBySubstringName(name);
        List<UserEntity> entitiesFiltered = new ArrayList<>();

        for(UserEntity entity : entities) {
            if(!entity.getId().equals(userEntity.getId())) {
                entitiesFiltered.add(entity);
            }
        }

        return entitiesFiltered;
    }
}
