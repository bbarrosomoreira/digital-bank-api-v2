package br.com.cdb.bancodigital.exceptions.custom;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
