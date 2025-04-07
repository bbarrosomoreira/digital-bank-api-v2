package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

public class AjustarLimiteDTO {
	
	private BigDecimal limiteNovo;
	
	public AjustarLimiteDTO () {}

	public BigDecimal getLimiteNovo() {
		return limiteNovo;
	}
	public void setLimiteNovo(BigDecimal limiteNovo) {
		this.limiteNovo = limiteNovo;
	}
	
	

}
