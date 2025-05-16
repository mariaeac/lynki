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

// OpenAPI imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/url")
@Validated
@Tag(name = "URL Shortener", description = "APIs para encurtar URLs e redirecionar")
public class UrlController {

    private final UrlService urlService;
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(
            summary = "Encurtar uma URL",
            description = "Recebe uma URL original e retorna uma URL encurtada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "URL encurtada criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UrlResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UrlResponseDTO> shorten(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo a URL a ser encurtada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UrlRequestDTO.class))
            )
            @Valid @RequestBody UrlRequestDTO urlRequest,
            HttpServletRequest request
    ) {
        UrlResponseDTO response = urlService.generateShortUrl(urlRequest, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Redirecionar para a URL original",
            description = "Redireciona para a URL original a partir do identificador encurtado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirecionamento para a URL original", content = @Content),
            @ApiResponse(responseCode = "404", description = "URL não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(
            @Parameter(
                    description = "Identificador da URL encurtada",
                    required = true,
                    example = "abc123"
            )
            @PathVariable String id
    ) {
        Url originUrl = urlService.redirectOriginUrl(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originUrl.getOriginUrl()));
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
