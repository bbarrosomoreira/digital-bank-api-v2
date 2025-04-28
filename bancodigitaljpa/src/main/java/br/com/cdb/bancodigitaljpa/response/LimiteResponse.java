package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.CartaoBase;
import br.com.cdb.bancodigitaljpa.model.CartaoCredito;
import br.com.cdb.bancodigitaljpa.model.CartaoDebito;

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
