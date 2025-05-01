package br.com.cdb.bancodigitaljpa.dto.response;

import br.com.cdb.bancodigitaljpa.model.Cartao;

import java.math.BigDecimal;


public record FaturaResponse (
		String numCartao,
		BigDecimal totalFaturaPaga,
		BigDecimal limiteAtual,
		BigDecimal limite
		) {
	
	public static FaturaResponse toFaturaResponse (Cartao ccr) {
		return new FaturaResponse(
				ccr.getNumeroCartao(),
				ccr.getTotalFaturaPaga(),
				ccr.getLimiteAtual(),
				ccr.getLimite()
				);
	}

}
