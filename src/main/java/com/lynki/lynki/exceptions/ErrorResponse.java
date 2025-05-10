package com.lynki.lynki.exceptions;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int httpStatus, String error, String message, String code) {
}
