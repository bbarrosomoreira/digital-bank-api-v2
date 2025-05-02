package br.com.cdb.bancodigital.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.EnderecoCliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
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
	
	@NotBlank(message = "CEP é obrigatório")
	@Pattern(regexp = "\\d{8}", message = "CEP deve estar no formato XXXXXXXX")
	private String cep;
	
	@NotNull(message = "Numero é um campo obrigatório - Se endereço não tiver número, digite 0")
	private int numero;
	
	@Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
	private String complemento;
	
	public EnderecoCliente transformaEnderecoParaObjeto() {
		EnderecoCliente enderecoCliente = new EnderecoCliente();
		enderecoCliente.setCep(cep);
		enderecoCliente.setNumero(numero);
		enderecoCliente.setComplemento(complemento);
		return enderecoCliente;
	}
	public Cliente transformaClienteParaObjeto() {
		Cliente cliente = new Cliente();
		cliente.setNome(nome);
		cliente.setCpf(cpf);
		cliente.setDataNascimento(dataNascimento);
		return cliente;
	}

	public ClienteDTO() {}
	

}
