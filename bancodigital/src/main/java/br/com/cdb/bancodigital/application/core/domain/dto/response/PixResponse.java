package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.math.BigDecimal;

public record PixResponse (
		String numContaOrigem,
		String numContaDestino,
		BigDecimal valor
		) {
	
	public static PixResponse toPixResponse(String numContaOrigem, String numContaDestino, BigDecimal valor) {
		return new PixResponse(
				numContaOrigem,
				numContaDestino,
				valor
				);
	}

}