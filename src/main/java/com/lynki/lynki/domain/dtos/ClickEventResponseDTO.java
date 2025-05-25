package com.lynki.lynki.domain.dtos;

import eu.bitwalker.useragentutils.DeviceType;

import java.time.Instant;

public record ClickEventResponseDTO(String shortUrl, String ip, DeviceType deviceType, Instant clickedAt, String os) {
}
