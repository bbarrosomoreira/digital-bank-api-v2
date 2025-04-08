package br.com.cdb.bancodigitaljpa.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String path,
        Map<String, String> fieldErrors
) {}
