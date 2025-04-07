package br.com.cdb.bancodigitaljpa.dto;

import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class ContratarSeguroDTO {
	
	private Long id_cartao;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoSeguro tipo;

	//G&S
	public Long getId_cartao() {
		return id_cartao;
	}
	public void setId_cartao(Long id_cartao) {
		this.id_cartao = id_cartao;
	}
	public TipoSeguro getTipo() {
		return tipo;
	}
	public void setTipo(TipoSeguro tipo) {
		this.tipo = tipo;
	}
	
	public ContratarSeguroDTO() {}

}
