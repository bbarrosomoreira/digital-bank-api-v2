package br.com.cdb.bancodigitaljpa.dto;

public class TipoSeguroResponse {
	private String nome;
	private String descricao;
	private String condicoes;
	
	//G&S
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getCondicoes() {
		return condicoes;
	}
	public void setCondicoes(String condicoes) {
		this.condicoes = condicoes;
	}
	
	//C
	public TipoSeguroResponse() {}
	public TipoSeguroResponse(String nome, String descricao, String condicoes) {
		this.nome = nome;
		this.descricao = descricao;
		this.condicoes = condicoes;
	}
	
}
