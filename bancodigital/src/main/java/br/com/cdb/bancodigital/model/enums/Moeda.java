package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Moeda {
	BRL ("Real Brasileiro", "R$"),
	USD ("Dólar Americano", "US$"),
	EUR ("Euro", "€");
	
	private final String nome;
	private final String simbolo;

	@JsonCreator
	public static Moeda fromString(String moedaStr) {
		if (moedaStr == null) {
			throw new IllegalArgumentException("Moeda não pode ser nula.");
		}
		return Arrays.stream(Moeda.values())
				.filter(moeda -> moeda.name().equalsIgnoreCase(moedaStr))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Moeda inválida: " + moedaStr +
						". Valores permitidos: " + Arrays.toString(Moeda.values())));
	}

}
