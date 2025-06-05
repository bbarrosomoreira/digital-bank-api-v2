package br.com.cdb.bancodigital.application.port.out.api;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.config.exceptions.custom.ValidationException;

public interface BrasilApiPort {
    CEP2 buscarEnderecoPorCep(String cep) throws ValidationException;
}
