package br.com.cdb.bancodigitaljpa.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoTransacao {
	SAQUE ("Saque"),
	DEPOSITO ("Depósito"),
	TRANSFERENCIA ("Transferência"),
	PIX ("PIX"),
	CREDITO ("Compra com cartão de crédito"),
	DEBITO ("Compra com cartão de débito"),
	COBRANCATARIFA ("Cobrança de tarifa"),
	RENDIMENTOS ("Rendimento de aplicação");
	
	private final String descricao;
	
	TipoTransacao (String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@JsonCreator
	public static TipoTransacao fromString(String value) {
		return TipoTransacao.valueOf(value.toUpperCase());
	}

}