package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.Cartao;

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
