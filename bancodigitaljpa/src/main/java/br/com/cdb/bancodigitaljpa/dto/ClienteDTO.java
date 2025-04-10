package br.com.cdb.bancodigitaljpa.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.EnderecoCliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteDTO {
	
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
	
	@Valid
	private EnderecoCliente endereco;
	
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
	public EnderecoCliente getEndereco() {
		return endereco;
	}
	public void setEndereco(EnderecoCliente endereco) {
		this.endereco = endereco;
	}
	
	//metodo
	public Cliente transformaParaObjeto() {
		return new Cliente(nome, cpf, dataNascimento, endereco);
	}
	public ClienteDTO() {}
	

}
