package com.lynki.lynki.repository;
import com.lynki.lynki.domain.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface  UrlRepository extends MongoRepository<Url, String> {

    Page<Url> findByUserId(String userId, Pageable pageable);

}
