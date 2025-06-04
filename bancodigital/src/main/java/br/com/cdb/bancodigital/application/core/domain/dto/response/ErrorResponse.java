package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path) {}

