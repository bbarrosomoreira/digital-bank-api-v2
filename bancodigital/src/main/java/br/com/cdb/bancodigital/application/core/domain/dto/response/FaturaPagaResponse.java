package br.com.cdb.bancodigital.application.core.domain.dto.response;

import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;

import java.math.BigDecimal;


public record FaturaPagaResponse (
        String numCartao,
        BigDecimal totalFaturaPaga,
        BigDecimal limiteAtual,
        BigDecimal limite
) {

    public static FaturaPagaResponse toFaturaPagaResponse (Cartao ccr) {
        return new FaturaPagaResponse(
                ccr.getNumeroCartao(),
                ccr.getTotalFaturaPaga(),
                ccr.getLimiteAtual(),
                ccr.getLimite()
        );
    }

}
