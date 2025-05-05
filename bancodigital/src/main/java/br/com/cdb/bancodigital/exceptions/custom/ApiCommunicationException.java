package br.com.cdb.bancodigital.exceptions.custom;

import org.springframework.http.HttpStatus;

public class ApiCommunicationException extends ApiException {

    public ApiCommunicationException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
