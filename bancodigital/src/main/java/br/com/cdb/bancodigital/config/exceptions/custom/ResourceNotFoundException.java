package br.com.cdb.bancodigital.config.exceptions.custom;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ResourceNotFoundException extends ApiException {

	@Serial
    private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
