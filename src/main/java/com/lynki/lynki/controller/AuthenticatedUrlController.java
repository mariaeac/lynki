package com.lynki.lynki.controller;

import com.lynki.lynki.domain.User;
import com.lynki.lynki.domain.dtos.UrlRequestDTO;
import com.lynki.lynki.domain.dtos.UrlResponseDTO;
import com.lynki.lynki.services.AuthenticatedUrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/url/auth")
public class AuthenticatedUrlController {

    private final AuthenticatedUrlService authenticatedUrlService;
    private final AuthenticationManager authenticationManager;

    public AuthenticatedUrlController(AuthenticatedUrlService authenticatedUrlService, AuthenticationManager authenticationManager) {
        this.authenticatedUrlService = authenticatedUrlService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping
    public ResponseEntity<UrlResponseDTO> shorten(@Valid @RequestBody UrlRequestDTO urlRequestDTO, HttpServletRequest request, Principal principal) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userId = user.getId();

        UrlResponseDTO response = authenticatedUrlService.urlShortGenerate(urlRequestDTO, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }







}
