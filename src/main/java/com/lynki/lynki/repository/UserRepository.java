package com.lynki.lynki.repository;

import com.lynki.lynki.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);
    User findByEmail(String email);
}
