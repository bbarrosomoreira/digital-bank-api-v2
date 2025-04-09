package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class AcionarSeguroFraudeDTO {
	
	@NotNull(message = "Valor da fraude é obrigatório")
	private BigDecimal valorFraude;
	
	//G&S
	public BigDecimal getValorFraude() {
		return valorFraude;
	}
	public void setValorFraude(BigDecimal valorFraude) {
		this.valorFraude = valorFraude;
	}
	
	public AcionarSeguroFraudeDTO() {}
	
}
