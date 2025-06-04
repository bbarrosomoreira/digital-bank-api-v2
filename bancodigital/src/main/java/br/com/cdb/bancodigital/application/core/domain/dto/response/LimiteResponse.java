package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.application.core.domain.model.Cartao;

public record LimiteResponse(
		String numCartao,
		BigDecimal limiteNovo,
		BigDecimal limiteAntigo
		){
	public static LimiteResponse toLimiteResponse (Cartao cartao, BigDecimal limiteNovo) {
		return new LimiteResponse(
				cartao.getNumeroCartao(),
				limiteNovo,
				cartao.getLimite());
	}

}
