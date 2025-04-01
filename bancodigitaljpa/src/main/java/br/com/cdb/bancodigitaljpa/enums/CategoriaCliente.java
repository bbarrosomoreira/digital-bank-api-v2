package br.com.cdb.bancodigitaljpa.enums;

public enum CategoriaCliente {
	COMUM("Comum", 0), //basic
	SUPER("Super", 1), //standard
	PREMIUM("Premium", 2);
	
	private final String descricao;
	private final int nivel;
	
	CategoriaCliente(String descricao, int nivel){
		this.descricao = descricao;
		this.nivel = nivel;
	}
	
	public static CategoriaCliente fromString(String value) {
		return valueOf(value.toUpperCase());
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public int getNivel() {
		return nivel;
	}

}
