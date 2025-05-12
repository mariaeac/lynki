package com.lynki.lynki.repository;
import com.lynki.lynki.domain.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface  UrlRepository extends MongoRepository<Url, String> {

    List<Url> findByUserId(String userId);

}
