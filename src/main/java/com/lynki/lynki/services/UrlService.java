package com.lynki.lynki.services;


import com.lynki.lynki.model.Url;
import com.lynki.lynki.model.dtos.UrlRequestDTO;
import com.lynki.lynki.model.dtos.UrlResponseDTO;
import com.lynki.lynki.repository.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public UrlResponseDTO generateShortUrl(UrlRequestDTO request, HttpServletRequest httpRequest) {
        String shortId = createUniqueID();
        Url url = new Url();
        url.setId(shortId);
        url.setOriginUrl(request.url());
        url.setExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));
        url.setClickCount(0L);

        urlRepository.save(url);

        String base = httpRequest.getRequestURL().toString().replace(httpRequest.getRequestURI(), "");
        String redirectLink = base + "/" + shortId;
        return new UrlResponseDTO(redirectLink, url.getClickCount());
    }

    public Url redirectOriginUrl(String shortId) {
        return urlRepository.findById(shortId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Url não encontrada!"));
    }




    private String createUniqueID() {

        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').filteredBy(Character::isLetterOrDigit).build();

        String id;
        do {
            id = generator.generate(8);

        } while (urlRepository.existsById(id));

        return id;
    }

}
