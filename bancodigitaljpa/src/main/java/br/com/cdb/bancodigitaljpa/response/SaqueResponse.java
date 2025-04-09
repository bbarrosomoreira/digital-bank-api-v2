package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

public record SaqueResponse (
		String numConta,
		BigDecimal valor,
		BigDecimal saldo
		) {
	
	public static SaqueResponse toSaqueResponse(String numConta, BigDecimal valor, BigDecimal saldo) {
		return new SaqueResponse(
				numConta,
				valor,
				saldo
				);
	}

}