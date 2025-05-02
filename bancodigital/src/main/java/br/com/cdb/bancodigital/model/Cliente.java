package br.com.cdb.bancodigital.model;

import java.time.LocalDate;

import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import lombok.*;

import br.com.cdb.bancodigital.model.enums.CategoriaCliente;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Cliente {

	private Long id;
	private String nome;
	private String cpf;
	private CategoriaCliente categoria;
	private LocalDate dataNascimento;
	private Usuario usuario;

	public boolean isMenorDeIdade() {
		if(this.dataNascimento == null) throw new InvalidInputParameterException("Data de Nascimento não pode ser nula");
		return this.dataNascimento.plusYears(18).isAfter(LocalDate.now());
	}

}
