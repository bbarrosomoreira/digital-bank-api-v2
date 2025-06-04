package br.com.cdb.bancodigital.application.port.out.api;

import br.com.cdb.bancodigital.application.core.domain.model.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ApiConversorMoedasResponse;

import java.math.BigDecimal;
import java.util.Optional;

public interface ConversorMoedasPort {
    ApiConversorMoedasResponse converterMoeda(Moeda from, Moeda to, BigDecimal amount);
    Optional<ApiConversorMoedasResponse> fazerConversao(Moeda from, Moeda to, BigDecimal amount);
    BigDecimal converterDeBrl(Moeda to, BigDecimal amount);
}
