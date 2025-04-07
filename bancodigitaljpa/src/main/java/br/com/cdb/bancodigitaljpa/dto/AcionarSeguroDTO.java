package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

public class AcionarSeguroDTO {
	
	private Long id_cartao;
	private BigDecimal valorFraude;
	
	//G&S
	public Long getId_cartao() {
		return id_cartao;
	}
	public void setId_cartao(Long id_cartao) {
		this.id_cartao = id_cartao;
	}
	public BigDecimal getValorFraude() {
		return valorFraude;
	}
	public void setValorFraude(BigDecimal valorFraude) {
		this.valorFraude = valorFraude;
	}
	
	public AcionarSeguroDTO() {}
	
}
