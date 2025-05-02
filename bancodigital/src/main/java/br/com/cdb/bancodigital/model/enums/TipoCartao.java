package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoCartao {
	CREDITO ("Cartão de Crédito"),
	DEBITO ("Cartão de Débito");
	
	private final String descricao;
	
	@JsonCreator
	public static TipoCartao fromString(String value) {
		return TipoCartao.valueOf(value.toUpperCase());
	}
}
