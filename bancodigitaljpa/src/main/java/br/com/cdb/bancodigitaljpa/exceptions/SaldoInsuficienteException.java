package br.com.cdb.bancodigitaljpa.exceptions;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private Long id_conta;
	private String numeroConta;
	private BigDecimal saldoAtual;

	public SaldoInsuficienteException(Long id_conta, String numeroConta, BigDecimal saldoAtual) {
		super(String.format("Saldo insuficiente na conta %s (ID: %d). Saldo atual: %.2f",
				numeroConta, id_conta, saldoAtual
				));
		this.id_conta = id_conta;
		this.numeroConta = numeroConta;
		this.saldoAtual = saldoAtual;
	}

	//G
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getId_conta() {
		return id_conta;
	}
	public String getNumConta() {
		return numeroConta;
	}
	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}
	
	
	

}
