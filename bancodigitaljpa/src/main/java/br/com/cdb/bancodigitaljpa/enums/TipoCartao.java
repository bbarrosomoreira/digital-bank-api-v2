package br.com.cdb.bancodigitaljpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoCartao {
	CREDITO ("Cartão de Crédito"),
	DEBITO ("Cartão de Débito");
	
	private final String descricao;
	
	TipoCartao (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@JsonCreator
	public static TipoCartao fromString(String value) {
		return TipoCartao.valueOf(value.toUpperCase());
	}
}
