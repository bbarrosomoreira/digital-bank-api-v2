package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;

public record RessetarLimiteDiarioResponse(
		String numCartao,
		BigDecimal limiteAtual
		){
	public static RessetarLimiteDiarioResponse toRessetarLimiteDiarioResponse (Cartao cartao) {
		return new RessetarLimiteDiarioResponse(
				cartao.getNumeroCartao(),
				cartao.getLimiteAtual());
	}

}