package br.com.cdb.bancodigital.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.EnderecoCliente;
import br.com.cdb.bancodigital.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class ClienteUsuarioDTO {

	//CLIENTE
	@NotBlank(message = NOME_OBRIGATORIO)
	@Size(min = 2, max = 100, message = NOME_TAMANHO)
	@Pattern(regexp = "^[\\p{L} ]+$", message = NOME_FORMATO)
	private String nome;
	
	@NotBlank(message = CPF_OBRIGATORIO)
	@CPF(message = CPF_INVALIDO)
	private String cpf;
	
	@Past(message = DATA_NASCIMENTO_PASSADO)
	@JsonFormat(pattern = FORMATO_DATA_DD_MM_YYYY)
	private LocalDate dataNascimento;
	
	@NotBlank(message = CEP_OBRIGATORIO)
	@Pattern(regexp = "\\d{8}", message = CEP_FORMATO)
	private String cep;
	
	@NotNull(message = NUMERO_OBRIGATORIO)
	private int numero;
	
	@Size(max = 100, message = COMPLEMENTO_TAMANHO)
	private String complemento;
	
	//USUARIO
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String senha;
	
	private Role role;

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
	

}
