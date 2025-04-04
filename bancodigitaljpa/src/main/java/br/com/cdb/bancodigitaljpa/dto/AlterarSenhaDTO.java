package br.com.cdb.bancodigitaljpa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AlterarSenhaDTO {
	
	@NotNull
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senhaAntiga;
	
	@NotNull
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senhaNova;

	//G&S
	public String getSenhaAntiga() {
		return senhaAntiga;
	}
	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}
	public String getSenhaNova() {
		return senhaNova;
	}
	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}
	
	//C
	public AlterarSenhaDTO() {}
	
	

}
