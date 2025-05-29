package br.com.cdb.bancodigital.exceptions.custom;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidInputParameterException extends ApiException {

	@Serial
    private static final long serialVersionUID = 1L;

	public InvalidInputParameterException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
