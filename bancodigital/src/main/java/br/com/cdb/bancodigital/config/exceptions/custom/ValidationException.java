package br.com.cdb.bancodigital.config.exceptions.custom;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ValidationException extends ApiException {
    
	@Serial
    private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
