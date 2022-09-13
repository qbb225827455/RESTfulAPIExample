package com.example.demo.Repository;

import com.example.demo.Model.User.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByEmailAddress(String email);
}