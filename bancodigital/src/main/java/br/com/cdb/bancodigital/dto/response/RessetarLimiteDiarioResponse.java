package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.model.Cartao;

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