package br.com.cdb.bancodigitaljpa.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.model.Cliente;
import br.com.cdb.bancodigitaljpa.model.EnderecoCliente;
import br.com.cdb.bancodigitaljpa.model.Usuario;
import br.com.cdb.bancodigitaljpa.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteUsuarioDTO {

	//CLIENTE
	@NotBlank(message = "Nome é obrigatótio")
	@Size(min=2, max=100, message="Nome deve ter entre 2 e 100 caracteres")
	@Pattern(regexp = "^[\\p{L} ]+$", message = "Nome deve conter apenas letras e espaços")
	private String nome;
	
	@NotBlank(message = "CPF é obrigatótio")
	@CPF(message = "CPF inválido")
	private String cpf;
	
	@Past(message = "Data de nascimento dever ser no passado")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataNascimento;
	
	@NotBlank(message = "CEP é obrigatório")
	@Pattern(regexp = "\\d{8}", message = "CEP deve estar no formato XXXXXXXX")
	private String cep;
	
	@NotNull(message = "Numero é um campo obrigatório - Se endereço não tiver número, digite 0")
	private int numero;
	
	@Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
	private String complemento;
	
	//USUARIO
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String senha;
	
	private Role role;
	
	
	//Getters and Setters
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	//metodo
	public Cliente transformaParaClienteObjeto() {
		EnderecoCliente endereco = new EnderecoCliente();
		return new Cliente(nome, cpf, dataNascimento, endereco);
	}
	
	public ClienteUsuarioDTO() {}
	

}
