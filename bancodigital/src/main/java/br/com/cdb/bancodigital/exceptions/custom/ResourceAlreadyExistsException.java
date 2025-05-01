package br.com.cdb.bancodigital.exceptions.custom;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends ApiException {

	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
