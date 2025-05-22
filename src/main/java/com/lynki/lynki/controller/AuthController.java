package com.lynki.lynki.controller;

import com.lynki.lynki.domain.dtos.LoginRequestDTO;
import com.lynki.lynki.domain.dtos.LoginResponseDTO;
import com.lynki.lynki.domain.dtos.RegisterUserDTO;
import com.lynki.lynki.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// OpenAPI imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "APIs para registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para registro do usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterUserDTO.class))
            )
            @RequestBody RegisterUserDTO registerUserDTO
    ) {
        authService.registerUser(registerUserDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Login do usuário",
            description = "Autentica um usuário e retorna um token de acesso."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais do usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequestDTO.class))
            )
            @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        LoginResponseDTO response = authService.login(loginRequestDTO);
        return ResponseEntity.ok().body(response);
    }


}
