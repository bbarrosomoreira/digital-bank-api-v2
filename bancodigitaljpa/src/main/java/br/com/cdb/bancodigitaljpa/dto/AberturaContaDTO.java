package br.com.cdb.bancodigitaljpa.dto;

import br.com.cdb.bancodigitaljpa.enums.TipoConta;

public class AberturaContaDTO {
	private Long id_cliente;
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
		this.tipoConta = TipoConta.valueOf(tipoConta.name().toUpperCase());
	}
	

}
