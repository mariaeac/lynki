package com.lynki.lynki.domain.dtos;

import com.lynki.lynki.domain.enums.UserRole;

public record RegisterUserDTO(String username, String password, String email) {
}
