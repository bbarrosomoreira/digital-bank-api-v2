package br.com.cdb.bancodigital.application.port.out.api;

import br.com.cdb.bancodigital.application.core.domain.model.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ApiConversorMoedasResponse;

import java.math.BigDecimal;

public interface ConversorMoedasPort {
    ApiConversorMoedasResponse converterMoeda(Moeda from, Moeda to, BigDecimal amount);
}
