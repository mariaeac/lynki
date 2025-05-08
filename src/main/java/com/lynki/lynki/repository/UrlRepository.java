package com.lynki.lynki.repository;


import com.lynki.lynki.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface  UrlRepository extends MongoRepository<Url, UUID> {

}
