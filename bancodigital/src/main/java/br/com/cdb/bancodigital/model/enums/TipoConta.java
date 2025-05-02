package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoConta {
	CORRENTE ("Conta Corrente"),
	POUPANCA ("Conta Poupanca"),
	INTERNACIONAL ("Conta Internacional");
	
	private final String descricao;
	
	@JsonCreator
	public static TipoConta fromString(String tipoContaStr) {
		if (tipoContaStr == null) {
			throw new IllegalArgumentException("Tipo de conta não pode ser nulo.");
		}
		for (TipoConta tipoConta : TipoConta.values()) {
			if (tipoConta.name().equalsIgnoreCase(tipoContaStr)) {
				return tipoConta;
			}
		}
		throw new IllegalArgumentException("Tipo de conta inválido: " + tipoContaStr +
				". Valores permitidos: " + Arrays.toString(TipoConta.values()));

	}

}
