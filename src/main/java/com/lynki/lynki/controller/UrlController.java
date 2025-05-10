package com.lynki.lynki.controller;

import com.lynki.lynki.domain.Url;
import com.lynki.lynki.domain.dtos.UrlRequestDTO;
import com.lynki.lynki.domain.dtos.UrlResponseDTO;
import com.lynki.lynki.services.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/url")
@Validated
public class UrlController {

    private final UrlService urlService;
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlResponseDTO> shorten(@Valid @RequestBody UrlRequestDTO urlRequest, HttpServletRequest request) {
        UrlResponseDTO response = urlService.generateShortUrl(urlRequest, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable @Size(min = 5, max = 10) String id) {
            Url originUrl = urlService.redirectOriginUrl(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(originUrl.getOriginUrl()));
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }



}
