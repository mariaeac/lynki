package com.lynki.lynki.repository;
import com.lynki.lynki.domain.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  UrlRepository extends MongoRepository<Url, String> {

    Page<Url> findByUserId(String userId, Pageable pageable);

    Page<Url> findByUserIdAndOriginUrlContainingIgnoreCase(String userId, String searchTerm, Pageable pageable);


}
