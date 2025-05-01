package br.com.cdb.bancodigitaljpa.model.enums;

public enum Moeda {
	BRL ("Real Brasileiro", "R$"),
	USD ("Dólar Americano", "US$"),
	EUR ("Euro", "€");
	
	private final String nome;
	private final String simbolo;
	
	Moeda (String nome, String simbolo) {
		this.nome = nome;
		this.simbolo = simbolo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getSimbolo() {
		return simbolo;
	}

}
