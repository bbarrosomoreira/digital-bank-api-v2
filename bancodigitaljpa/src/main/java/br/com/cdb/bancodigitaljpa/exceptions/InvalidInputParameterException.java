package br.com.cdb.bancodigitaljpa.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputParameterException extends ApiException {

	private static final long serialVersionUID = 1L;

	public InvalidInputParameterException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
