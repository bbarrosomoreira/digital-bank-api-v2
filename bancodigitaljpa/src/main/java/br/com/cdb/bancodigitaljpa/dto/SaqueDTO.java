package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

public class SaqueDTO {
	
	@Positive(message = "O valor deve ser positivo")
	@DecimalMin(value = "1.00", message = "O valor mínimo é R$1,00")
	private BigDecimal valor;

	//G&S
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	//constructor
	public SaqueDTO() {
	}
}