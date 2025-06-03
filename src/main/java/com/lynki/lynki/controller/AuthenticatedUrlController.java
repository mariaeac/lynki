package com.lynki.lynki.controller;

import com.lynki.lynki.domain.Url;
import com.lynki.lynki.domain.dtos.*;
import com.lynki.lynki.infra.TokenService;
import com.lynki.lynki.services.AuthenticatedUrlService;
import com.lynki.lynki.services.ClickEventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

// OpenAPI imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/url/auth")
@Tag(name = "URL Autenticada", description = "APIs para encurtar URLs e consultar URLs do usuário autenticado")
public class AuthenticatedUrlController {

    private final AuthenticatedUrlService authenticatedUrlService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ClickEventService clickEventService;

    public AuthenticatedUrlController(
            AuthenticatedUrlService authenticatedUrlService,
            AuthenticationManager authenticationManager,
            TokenService tokenService, ClickEventService clickEventService
    ) {
        this.authenticatedUrlService = authenticatedUrlService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.clickEventService = clickEventService;
    }

    @Operation(
            summary = "Encurtar URL autenticado",
            description = "Gera uma URL encurtada associada ao usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "URL encurtada criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UrlResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UrlResponseDTO> shorten(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo a URL a ser encurtada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UrlRequestDTO.class))
            )
            @Valid @RequestBody UrlRequestDTO urlRequestDTO,
            HttpServletRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = tokenService.getIdFromToken(authentication);

        UrlResponseDTO response = authenticatedUrlService.urlShortGenerate(urlRequestDTO, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @Operation(
            summary = "Listar URLs do usuário autenticado",
            description = "Retorna todas as URLs encurtadas associadas ao usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de URLs do usuário",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserUrlsResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    @GetMapping("/urls")
    public ResponseEntity<Page<UserUrlsResponseDTO>> getAllUserUrls(
            HttpServletRequest request,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) String search
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = tokenService.getIdFromToken(authentication);

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());

        Page<UserUrlsResponseDTO> response = authenticatedUrlService.getUrlsFromUser(userId, pageable, search, baseUrl);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Deletar uma URL", description = "Deleta uma URL de um usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "204", description = "URL Deletada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UrlResponseDTO.class))),
    })
    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortUrl) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = tokenService.getIdFromToken(authentication);

        authenticatedUrlService.deleteUrlById(shortUrl, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Informações do usuário", description = "Traz informações de nome e e-mail do usuário autenticado")
    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getUserInfo(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = tokenService.getIdFromToken(authentication);
        UserResponseDTO response = authenticatedUrlService.getUserInfo(userId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/analytics/{shortUrl}")
    public ResponseEntity<List<ClickEventResponseDTO>> getUrlClickEvents(@PathVariable String shortUrl) {

        List<ClickEventResponseDTO> response = clickEventService.getClickEventsByShortUrl(shortUrl);
        return ResponseEntity.ok().body(response);

    }







}
