package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.CartaoDebito;

public record PagamentoResponse (
		String numCartao,
		String tipoCartao,
		String descricao,
		BigDecimal valor,
		BigDecimal limiteAtual
		){
	public static PagamentoResponse toPagamentoResponse (CartaoBase cartao, BigDecimal valor, String descricao) {
		return new PagamentoResponse(
				cartao.getNumeroCartao(),
				cartao.getTipo(),
				descricao,
				valor,
				(cartao instanceof CartaoCredito) ? ((CartaoCredito) cartao).getLimiteAtual() :
					(cartao instanceof CartaoDebito) ? ((CartaoDebito) cartao).getLimiteAtual() : null);
	}

}
