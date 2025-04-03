package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;

public record FaturaResponse (
		Long id,
		String numCartao,
		BigDecimal totalFatura,
		BigDecimal limiteAtual,
		BigDecimal limiteCredito
		//add vencimento fatura
		) {
	
	public static FaturaResponse fromCartaoCredito (CartaoCredito ccr) {
		return new FaturaResponse(
				ccr.getId_cartao(),
				ccr.getNumeroCartao(),
				ccr.getTotalFatura(),
				ccr.getLimiteAtual(),
				ccr.getLimiteCredito()
				);
	}

}
