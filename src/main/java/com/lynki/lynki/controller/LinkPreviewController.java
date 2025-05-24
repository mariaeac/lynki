package com.lynki.lynki.controller;

import com.lynki.lynki.domain.dtos.LinkPreviewResponseDTO;
import com.lynki.lynki.services.LinkPreviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/preview")
public class LinkPreviewController {


    private LinkPreviewService linkPreviewService;

    public LinkPreviewController(LinkPreviewService linkPreviewService) {
        this.linkPreviewService = linkPreviewService;
    }

    @GetMapping
    public ResponseEntity<LinkPreviewResponseDTO> getLinkPreview(@RequestParam String url) {

        LinkPreviewResponseDTO linkPreviewResponse = linkPreviewService.fetchLinkPreview(url);
        return ResponseEntity.ok(linkPreviewResponse);
    }

}
