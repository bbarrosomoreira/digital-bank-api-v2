package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoCartao {
	CREDITO ("Cartão de Crédito"),
	DEBITO ("Cartão de Débito");
	
	private final String descricao;
	
	@JsonCreator
	public static TipoCartao fromString(String tipoCartaoStr) {
		if (tipoCartaoStr == null) {
			throw new IllegalArgumentException("Tipo de cartão não pode ser nulo.");
		}
		for (TipoCartao tipoCartao : TipoCartao.values()) {
			if (tipoCartao.name().equalsIgnoreCase(tipoCartaoStr)) {
				return tipoCartao;
			}
		}
		throw new IllegalArgumentException("Tipo de cartão inválido: " + tipoCartaoStr +
				". Valores permitidos: " + Arrays.toString(TipoCartao.values()));

	}
}
