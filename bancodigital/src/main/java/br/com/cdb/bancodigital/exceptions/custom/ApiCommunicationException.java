package br.com.cdb.bancodigital.exceptions.custom;

import org.springframework.http.HttpStatus;

public class ApiCommunicationException extends ApiException {

    public ApiCommunicationException(String message, Throwable cause) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
        initCause(cause);
    }
}
