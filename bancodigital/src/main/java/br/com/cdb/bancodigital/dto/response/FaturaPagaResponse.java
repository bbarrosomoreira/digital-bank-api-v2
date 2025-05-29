package br.com.cdb.bancodigital.dto.response;

import br.com.cdb.bancodigital.model.Cartao;

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
