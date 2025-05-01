package br.com.cdb.bancodigital.dto.response;

import java.time.LocalDate;
import java.time.Period;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;

public class ClienteResponse {
	
	private Long id;
	private String nome;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataNascimento;
	private Integer idade;
	private EnderecoResponse endereco;
	private CategoriaCliente categoria;
	
	//G
	public Long getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public Integer getIdade() {
		return idade;
	}
	public EnderecoResponse getEndereco() {
		return endereco;
	}
	public CategoriaCliente getCategoria() {
		return categoria;
	}
	
	//C
	public ClienteResponse () {}
	public ClienteResponse(Cliente cliente) {
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.dataNascimento = cliente.getDataNascimento();
		this.idade = Period.between(cliente.getDataNascimento(), LocalDate.now()).getYears();
		this.endereco = new EnderecoResponse(cliente.getEndereco());
		this.categoria = cliente.getCategoria();
	}
	

}
