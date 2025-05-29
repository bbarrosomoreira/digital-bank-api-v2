package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;

public record DepositoResponse (
		String numConta,
		BigDecimal valor,
		BigDecimal saldo
		) {
	
	public static DepositoResponse toDepositoResponse(String numConta, BigDecimal valor, BigDecimal saldo) {
		return new DepositoResponse(
				numConta,
				valor,
				saldo
				);
	}

}