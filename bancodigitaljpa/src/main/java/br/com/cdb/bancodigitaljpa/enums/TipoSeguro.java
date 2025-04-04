package br.com.cdb.bancodigitaljpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoSeguro {
	VIAGEM ("Seguro de Viagem", "Cobertura para despesas médicas no exterior, cancelamento de voos e extravio de bagagem."),
	FRAUDE ("Seguro de Fraude", "Cobertura automática para fraudes no cartão, com um valor base de R$5.000,00");
	
	private final String nome;
	private final String descricao;
	
	TipoSeguro (String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}
	
	public String getNome() {
		return nome;
	}
	public String getDescricao() {
		return descricao;
	}
	
	@JsonCreator
	public static TipoSeguro fromString (String value) {
		return TipoSeguro.valueOf(value.toUpperCase());
	}

}
