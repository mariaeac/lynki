package com.lynki.lynki.controller;

import com.lynki.lynki.domain.dtos.UrlRequestDTO;
import com.lynki.lynki.domain.dtos.UrlResponseDTO;
import com.lynki.lynki.domain.dtos.UserUrlsResponseDTO;
import com.lynki.lynki.infra.TokenService;
import com.lynki.lynki.services.AuthenticatedUrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/url/auth")
public class AuthenticatedUrlController {

    private final AuthenticatedUrlService authenticatedUrlService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticatedUrlController(AuthenticatedUrlService authenticatedUrlService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticatedUrlService = authenticatedUrlService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    @PostMapping
    public ResponseEntity<UrlResponseDTO> shorten(@Valid @RequestBody UrlRequestDTO urlRequestDTO, HttpServletRequest request) {

       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userId = tokenService.getIdFromToken(authentication);

        UrlResponseDTO response = authenticatedUrlService.urlShortGenerate(urlRequestDTO, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserUrlsResponseDTO>> getAllUserUrls(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String userId = tokenService.getIdFromToken(authentication);
        List<UserUrlsResponseDTO> response = authenticatedUrlService.getUrlsFromUser(userId);
        return ResponseEntity.ok().body(response);
    }






}
