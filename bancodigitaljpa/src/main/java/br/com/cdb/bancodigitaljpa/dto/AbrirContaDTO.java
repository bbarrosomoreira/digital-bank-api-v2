package br.com.cdb.bancodigitaljpa.dto;

import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class AbrirContaDTO {
	
	private Long id_cliente;
	
	@NotNull(message = "Tipo de conta é obrigatório")
	@Enumerated(EnumType.STRING)
	private TipoConta tipoConta;
	
	//G&S
	public Long getId_cliente() {
		return id_cliente;
	}
	public void setId_cliente(Long id_cliente) {
		this.id_cliente = id_cliente;
	}
	public TipoConta getTipoConta() {
		return tipoConta;
	}
	public void setTipoConta(String tipoConta) {
		this.tipoConta = TipoConta.valueOf(tipoConta.toUpperCase());
	}
	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}
	
	public AbrirContaDTO() {}

}
