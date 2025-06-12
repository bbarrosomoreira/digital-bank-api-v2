package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;

public record PagamentoResponse (
		String numCartao,
		String tipoCartao,
		String descricao,
		BigDecimal valor,
		BigDecimal limiteAtual
		){
	public static PagamentoResponse toPagamentoResponse (Cartao cartao, BigDecimal valor, String descricao) {
		return new PagamentoResponse(
				cartao.getNumeroCartao(),
				cartao.getTipoCartao().getDescricao(),
				descricao,
				valor,
                cartao.getLimiteAtual());
	}

}
