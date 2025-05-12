package com.lynki.lynki.domain.dtos;

import java.time.Instant;

public record UserUrlsResponseDTO(String originUrl, String shortUrl, Instant expiresAt, Long clicksCount ) {
}
