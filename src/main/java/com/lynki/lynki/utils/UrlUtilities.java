package com.lynki.lynki.utils;

import com.lynki.lynki.repository.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;

@Component
public class UrlUtilities {

    private final UrlRepository urlRepository;
    public UrlUtilities(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    public String createUniqueID() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').filteredBy(Character::isLetterOrDigit).build();

        String id;
        do {
            id = generator.generate(10);

        } while (urlRepository.existsById(id));
        return  id;
    }

    public String buildRedirectLink(HttpServletRequest request, String shortId) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return baseUrl + "/api/v1/url/" + shortId;
    }

}
