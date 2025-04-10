package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class TesteConversorMoedasDTO {
	@NotNull(message = "Moeda da conta é obrigatório")
	@Enumerated(EnumType.STRING)
	private Moeda moeda;
	
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valor;
	
	//C
	public TesteConversorMoedasDTO(){}

	//G&S
	public Moeda getMoeda() {
		return moeda;
	}
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
}
