package br.com.cdb.bancodigitaljpa.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnderecoCliente {

	@NotBlank(message = "CEP é obrigatório")
	@Pattern(regexp = "\\d{8}", message = "CEP deve estar no formato XXXXXXXX")
	private String cep;

	@NotBlank(message = "Rua é um campo obrigatório")
	@Size(max = 100)
	private String rua;

	@NotNull(message = "Numero é um campo obrigatório - Se endereço não tiver número, digite 0")
	private int numero;

	@Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
	private String complemento;

	@NotBlank(message = "Bairro é um campo obrigatório")
	@Size(max = 100)
	private String bairro;

	@NotBlank(message = "Cidade é obrigatória")
    @Size(max = 50, message = "Cidade deve ter no máximo 50 caracteres")
	private String cidade;

	@NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (ex: SP)")
	private String estado;

	private boolean enderecoPrincipal;

	
	//getters and setters
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public boolean isEnderecoPrincipal() {
		return enderecoPrincipal;
	}
	public void setEnderecoPrincipal(boolean enderecoPrincipal) {
		this.enderecoPrincipal = enderecoPrincipal;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	//construtor
	public EnderecoCliente(String rua, int numero, String complemento, String bairro, String cidade, String estado, String cep, boolean enderecoPrincipal) {
		super();
		this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
		this.enderecoPrincipal = enderecoPrincipal;
	}
	public EnderecoCliente() {	
	}
	

}
