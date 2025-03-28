package br.com.cdb.bancodigitaljpa.enums;

public enum TipoConta {
	CORRENTE ("Conta Corrente"),
	POUPANCA ("Conta Poupanca");
	
	private final String descricao;
	
	TipoConta (String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
