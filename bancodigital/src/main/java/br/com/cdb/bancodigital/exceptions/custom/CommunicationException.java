package br.com.cdb.bancodigital.exceptions.custom;

import org.springframework.http.HttpStatus;

public class CommunicationException extends ApiException {

    public CommunicationException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
