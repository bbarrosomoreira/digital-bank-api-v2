package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoSeguro {
	VIAGEM ("Seguro de Viagem", "Cobertura para despesas médicas no exterior, cancelamento de voos e extravio de bagagem, com um valor base de R$10.000,00.", "Clientes Comum e Super: opcional por R$50,00 por mês. Clientes Premium: isento de tarifa."),
	FRAUDE ("Seguro de Fraude", "Cobertura automática para fraudes no cartão, com um valor base de R$5.000,00", "Serviço gratuito para todas as categorias de clientes.");
	
	private final String nome;
	private final String descricao;
	private final String condicoes;
	
	TipoSeguro (String nome, String descricao, String condicoes) {
		this.nome = nome;
		this.descricao = descricao;
		this.condicoes = condicoes;
	}
	
	public String getNome() {
		return nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getCondicoes() {
		return condicoes;
	}

	@JsonCreator
	public static TipoSeguro fromString (String value) {
		return TipoSeguro.valueOf(value.toUpperCase());
	}

}
