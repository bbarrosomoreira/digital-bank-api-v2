package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.time.LocalDate;
import java.time.Period;

import br.com.cdb.bancodigital.application.core.domain.model.EnderecoCliente;
import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteResponse {
	
	private Long id;
	private String nome;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataNascimento;
	private Integer idade;
	private EnderecoResponse endereco;
	private CategoriaCliente categoria;

	public ClienteResponse(Cliente cliente, EnderecoCliente endereco) {
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.dataNascimento = cliente.getDataNascimento();
		this.idade = Period.between(cliente.getDataNascimento(), LocalDate.now()).getYears();
		this.endereco = new EnderecoResponse(endereco);
		this.categoria = cliente.getCategoria();
	}
	

}
