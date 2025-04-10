package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class ConversorMoedasDTO {
	@NotNull(message = "Moeda da conta é obrigatório")
	@Enumerated(EnumType.STRING)
	private Moeda moedaOrigem;
	
	@NotNull(message = "Moeda da conta é obrigatório")
	@Enumerated(EnumType.STRING)
	private Moeda moedaDestino;
	
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valor;
	
	//C
	public ConversorMoedasDTO(){}

	//G&S
	public BigDecimal getValor() {
		return valor;
	}
	public Moeda getMoedaOrigem() {
		return moedaOrigem;
	}
	public void setMoedaOrigem(Moeda moedaOrigem) {
		this.moedaOrigem = moedaOrigem;
	}
	public Moeda getMoedaDestino() {
		return moedaDestino;
	}
	public void setMoedaDestino(Moeda moedaDestino) {
		this.moedaDestino = moedaDestino;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
}
