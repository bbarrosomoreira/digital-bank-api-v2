package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoDebito;

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