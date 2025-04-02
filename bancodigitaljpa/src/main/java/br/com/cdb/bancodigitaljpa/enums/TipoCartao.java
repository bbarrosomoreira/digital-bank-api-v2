package br.com.cdb.bancodigitaljpa.enums;

public enum TipoCartao {
	CREDITO ("Cartão de Crédito"),
	DEBITO ("Cartão de Débito");
	
	private final String descricao;
	
	TipoCartao (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
