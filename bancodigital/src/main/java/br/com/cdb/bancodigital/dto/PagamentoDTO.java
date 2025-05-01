package br.com.cdb.bancodigital.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class PagamentoDTO {
	//valor e descricao e senha
	//extra: data e tipoCartao para verificar se estou comprando no crédito mesmo ou no débito)
	
	@Positive(message = "O valor deve ser positivo")
	private BigDecimal valor;
	
	@Size(min=0, max=100, message="A descrição deve ter no máximo 100 caracteres.")
	private String descricao;
	
	@NotNull
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senha;

	//G&S
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	//C
	public PagamentoDTO() {	}
	

	
	
	

}
