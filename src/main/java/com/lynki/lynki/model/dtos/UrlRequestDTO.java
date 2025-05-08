package com.lynki.lynki.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UrlRequestDTO(@NotBlank String url, @Min(0) long expirationTime) {
}
