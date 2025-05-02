package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoConta {
	CORRENTE ("Conta Corrente"),
	POUPANCA ("Conta Poupanca"),
	INTERNACIONAL ("Conta Internacional");
	
	private final String descricao;
	
	@JsonCreator
	public static TipoConta fromString(String value) {
		return TipoConta.valueOf(value.toUpperCase());
	}

}
