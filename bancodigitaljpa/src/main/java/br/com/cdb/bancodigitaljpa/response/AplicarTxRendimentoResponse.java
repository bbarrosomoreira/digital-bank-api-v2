package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;

public class AplicarTxRendimentoResponse {
	private String numConta;
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valor;
	@Digits(integer = 19, fraction = 2)
	private BigDecimal saldo;
	
	//G
	public String getNumConta() {
		return numConta;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}

	//C
	public AplicarTxRendimentoResponse (String numConta, BigDecimal valor, BigDecimal saldo) {
		this.numConta = numConta;
		this.valor = valor;
		this.saldo = saldo;
	}
	public AplicarTxRendimentoResponse() {}

	public static AplicarTxRendimentoResponse toAplicarTxRendimentoResponse(String numConta, BigDecimal valor, BigDecimal saldo) {
		return new AplicarTxRendimentoResponse(
				numConta,
				valor,
				saldo
				);
	}

}