package br.com.cdb.bancodigital.model.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
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

	public static Moeda fromString(String moedaStr) {
		if(moedaStr == null) {
			throw new IllegalArgumentException("Categoria não pode ser nula.");
		}
		for (Moeda moeda : Moeda.values()) {
			if (moeda.name().equalsIgnoreCase(moedaStr)){
				return moeda;
			}
		}

		throw new IllegalArgumentException("Moeda inválida: " + moedaStr +
				". Valores permitidos: " + Arrays.toString(Moeda.values()));
	}

}
