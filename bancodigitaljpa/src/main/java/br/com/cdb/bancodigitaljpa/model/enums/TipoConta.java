package br.com.cdb.bancodigitaljpa.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoConta {
	CORRENTE ("Conta Corrente"),
	POUPANCA ("Conta Poupanca"),
	INTERNACIONAL ("Conta Internacional");
	
	private final String descricao;
	
	TipoConta (String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@JsonCreator
	public static TipoConta fromString(String value) {
		return TipoConta.valueOf(value.toUpperCase());
	}

}
