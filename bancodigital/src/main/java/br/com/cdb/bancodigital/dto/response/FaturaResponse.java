package br.com.cdb.bancodigital.dto.response;

import br.com.cdb.bancodigital.model.Cartao;

import java.math.BigDecimal;


public record FaturaResponse (
		String numCartao,
		BigDecimal totalFatura,
		BigDecimal limiteAtual,
		BigDecimal limite
		) {
	
	public static FaturaResponse toFaturaResponse (Cartao ccr) {
		return new FaturaResponse(
				ccr.getNumeroCartao(),
				ccr.getTotalFatura(),
				ccr.getLimiteAtual(),
				ccr.getLimite()
				);
	}

}
