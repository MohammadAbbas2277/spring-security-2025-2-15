package com.java24.spring_securitity_demo.repositories;

import com.java24.spring_securitity_demo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // this row help me to hande the login method.
    Optional<User> findByUsername(String username);
}