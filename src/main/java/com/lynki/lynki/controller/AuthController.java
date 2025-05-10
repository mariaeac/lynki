package com.lynki.lynki.controller;

import com.lynki.lynki.domain.dtos.LoginRequestDTO;
import com.lynki.lynki.domain.dtos.LoginResponseDTO;
import com.lynki.lynki.domain.dtos.RegisterUserDTO;
import com.lynki.lynki.repository.UserRepository;
import com.lynki.lynki.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")


public class AuthController {

   private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegisterUserDTO registerUserDTO) {

        authService.registerUser(registerUserDTO);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = authService.login(loginRequestDTO);
        return ResponseEntity.ok().body(response);
    }





}
