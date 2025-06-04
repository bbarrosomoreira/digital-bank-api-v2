package br.com.cdb.bancodigital.application.core.domain.dto.response;

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
