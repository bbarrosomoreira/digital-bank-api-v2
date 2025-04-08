package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;

public record FaturaResponse (
		String numCartao,
		BigDecimal totalFatura,
		BigDecimal limiteAtual,
		BigDecimal limiteCredito
		) {
	
	public static FaturaResponse toFaturaResponse (CartaoCredito ccr) {
		return new FaturaResponse(
				ccr.getNumeroCartao(),
				ccr.getTotalFatura(),
				ccr.getLimiteAtual(),
				ccr.getLimiteCredito()
				);
	}

}
