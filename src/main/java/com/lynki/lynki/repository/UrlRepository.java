package com.lynki.lynki.repository;
import com.lynki.lynki.domain.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  UrlRepository extends MongoRepository<Url, String> {

}
