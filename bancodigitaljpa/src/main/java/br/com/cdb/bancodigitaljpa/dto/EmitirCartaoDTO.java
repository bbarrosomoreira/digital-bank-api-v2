package br.com.cdb.bancodigitaljpa.dto;

import br.com.cdb.bancodigitaljpa.model.enums.TipoCartao;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EmitirCartaoDTO {
	
	private Long id_conta;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoCartao tipoCartao;
	
	@NotNull
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senha;

	//G&S
	public Long getId_conta() {
		return id_conta;
	}
	public void setId_conta(Long id_conta) {
		this.id_conta = id_conta;
	}
	public TipoCartao getTipoCartao() {
		return tipoCartao;
	}
	public void setTipoCartao(TipoCartao tipoCartao) {
		this.tipoCartao = tipoCartao;
	}
	public void setTipoCartao(String tipoCartao) {
		this.tipoCartao = TipoCartao.valueOf(tipoCartao.toUpperCase());
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getSenha() {
		return senha;
	}
	
	//C
	public EmitirCartaoDTO() {}
	
	
	

}
