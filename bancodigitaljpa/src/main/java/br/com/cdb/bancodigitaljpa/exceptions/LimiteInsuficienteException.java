package br.com.cdb.bancodigitaljpa.exceptions;

import java.math.BigDecimal;

public class LimiteInsuficienteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private Long id_cartao;
	private String numeroCartao;
	private BigDecimal limiteAtual;

	public LimiteInsuficienteException(Long id_cartao, String numeroCartao, BigDecimal limiteAtual) {
		super(String.format("Limite insuficiente no cartão %s (ID: %d). Limite atual: %.2f",
				numeroCartao, id_cartao, limiteAtual
				));
		this.id_cartao = id_cartao;
		this.numeroCartao = numeroCartao;
		this.limiteAtual = limiteAtual;
	}

	//G
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getId_cartao() {
		return id_cartao;
	}
	public String getNumCartao() {
		return numeroCartao;
	}
	public BigDecimal getLimiteAtual() {
		return limiteAtual;
	}
}
