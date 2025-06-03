package com.lynki.lynki.controller;

import com.lynki.lynki.domain.Url;
import com.lynki.lynki.services.AuthenticatedUrlService;
import com.lynki.lynki.services.UrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController

public class RedirectController {

    private final UrlService urlService;
    private final AuthenticatedUrlService authenticatedUrlService;

    public RedirectController(UrlService urlService, AuthenticatedUrlService authenticatedUrlService) {
        this.urlService = urlService;
        this.authenticatedUrlService = authenticatedUrlService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable String id, Authentication authentication) {
        Url originUrl = null;
        Url url = urlService.findById(id);
        if (url == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (url.isPrivate()) {

            originUrl = authenticatedUrlService.redirectOriginUrl(id);

        } else {
            originUrl = urlService.redirectOriginUrl(id);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originUrl.getOriginUrl()));
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
