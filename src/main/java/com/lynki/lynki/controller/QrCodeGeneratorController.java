package com.lynki.lynki.controller;

import com.lynki.lynki.domain.dtos.UrlRequestDTO;
import com.lynki.lynki.services.QrCodeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping("/api/v1/url/")
public class QrCodeGeneratorController {

    private final QrCodeService qrCodeService;
    public QrCodeGeneratorController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQrCode(@RequestBody UrlRequestDTO url) throws Exception {
        return qrCodeService.generateQRCodePng(url.url());
    }
}
