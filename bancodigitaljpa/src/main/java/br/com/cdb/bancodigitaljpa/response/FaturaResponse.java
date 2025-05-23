package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;

public record FaturaResponse (
		String numCartao,
		BigDecimal totalFaturaPaga,
		BigDecimal limiteAtual,
		BigDecimal limiteCredito
		) {
	
	public static FaturaResponse toFaturaResponse (CartaoCredito ccr) {
		return new FaturaResponse(
				ccr.getNumeroCartao(),
				ccr.getTotalFaturaPaga(),
				ccr.getLimiteAtual(),
				ccr.getLimiteCredito()
				);
	}

}
