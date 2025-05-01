package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.CartaoBase;
import br.com.cdb.bancodigitaljpa.model.CartaoDebito;

public record RessetarLimiteDiarioResponse(
		String numCartao,
		BigDecimal limiteAtual
		){
	public static RessetarLimiteDiarioResponse toRessetarLimiteDiarioResponse (CartaoBase cartao) {
		return new RessetarLimiteDiarioResponse(
				cartao.getNumeroCartao(),
				((CartaoDebito) cartao).getLimiteAtual());
	}

}