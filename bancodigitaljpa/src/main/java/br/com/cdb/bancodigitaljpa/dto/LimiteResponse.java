package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.CartaoDebito;

public record LimiteResponse(
		String numCartao,
		BigDecimal limiteNovo,
		BigDecimal limiteAntigo
		){
	public static LimiteResponse toLimiteResponse (CartaoBase cartao, BigDecimal limiteNovo) {
		return new LimiteResponse(
				cartao.getNumeroCartao(),
				limiteNovo,
				(cartao instanceof CartaoCredito) ? ((CartaoCredito) cartao).getLimiteCredito() :
					(cartao instanceof CartaoDebito) ? ((CartaoDebito) cartao).getLimiteDiario() : null);
	}

}
