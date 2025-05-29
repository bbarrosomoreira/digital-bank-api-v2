package br.com.cdb.bancodigital.exceptions.custom;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class SystemException extends ApiException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SystemException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
