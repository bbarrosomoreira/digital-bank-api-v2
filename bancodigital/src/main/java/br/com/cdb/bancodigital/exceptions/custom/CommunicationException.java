package br.com.cdb.bancodigital.exceptions.custom;

import org.springframework.http.HttpStatus;

public class CommunicationException extends ApiException {

    public CommunicationException(String message, Throwable cause) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
