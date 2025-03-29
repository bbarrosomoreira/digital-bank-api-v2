package br.com.cdb.bancodigitaljpa.dto;

import java.time.LocalDate;

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.EnderecoCliente;

public class ClienteDTO {
	
	private String nome;
	private String cpf;
	private LocalDate dataNascimento;
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
	

}
