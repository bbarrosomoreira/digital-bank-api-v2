package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.Cartao;

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