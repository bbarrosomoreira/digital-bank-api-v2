package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

public record AplicarTxManutencaoResponse (
		String numConta,
		BigDecimal valor,
		BigDecimal saldo
		) {
	
	public static AplicarTxManutencaoResponse toAplicarTxManutencaoResponse(String numConta, BigDecimal valor, BigDecimal saldo) {
		return new AplicarTxManutencaoResponse(
				numConta,
				valor,
				saldo
				);
	}

}