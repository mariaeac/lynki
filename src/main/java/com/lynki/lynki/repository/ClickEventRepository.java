package com.lynki.lynki.repository;

import com.lynki.lynki.domain.ClickEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClickEventRepository extends MongoRepository<ClickEvent, Long> {

    List<ClickEvent> findByShortUrl(String shortUrl);
}
