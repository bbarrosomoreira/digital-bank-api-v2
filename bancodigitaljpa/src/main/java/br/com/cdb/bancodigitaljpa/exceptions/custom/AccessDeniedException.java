package br.com.cdb.bancodigitaljpa.exceptions.custom;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApiException {
    
	private static final long serialVersionUID = 1L;

	public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
