package br.com.cdb.bancodigital.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public abstract class ApiException extends RuntimeException {

	@Serial
    private static final long serialVersionUID = 1L;
	
	private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
