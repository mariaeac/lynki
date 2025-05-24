package com.lynki.lynki.controller;

import com.lynki.lynki.domain.dtos.UserUrlsResponseDTO;
import com.lynki.lynki.infra.TokenService;
import com.lynki.lynki.services.AuthenticatedUrlService;
import com.lynki.lynki.services.ExportUrlService;

import org.apache.coyote.Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/export")
public class ExportController {

    public final ExportUrlService exportUrlService;
    private final TokenService tokenService;

    public ExportController(ExportUrlService exportUrlService, TokenService tokenService) {
        this.exportUrlService = exportUrlService;
        this.tokenService = tokenService;
    }

    @GetMapping("/json")
    public ResponseEntity<List<UserUrlsResponseDTO>> exportAsJson() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = tokenService.getIdFromToken(authentication);
        List<UserUrlsResponseDTO> response = exportUrlService.generateJson(userId);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/csv")
    public ResponseEntity<Resource> exportAsCsv() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = tokenService.getIdFromToken(authentication);

        String csv = exportUrlService.generateCsv(userId);
        ByteArrayResource resource = new ByteArrayResource(csv.getBytes());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=short_urls.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);

    }
}
