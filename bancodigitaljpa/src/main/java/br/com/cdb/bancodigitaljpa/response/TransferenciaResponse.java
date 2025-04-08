package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

public record TransferenciaResponse (
		String numContaOrigem,
		String numContaDestino,
		BigDecimal valor
		) {
	
	public static TransferenciaResponse toTransferenciaResponse(String numContaOrigem, String numContaDestino, BigDecimal valor) {
		return new TransferenciaResponse(
				numContaOrigem,
				numContaDestino,
				valor
				);
	}

}
