package br.com.cdb.bancodigital.application.core.domain.entity;

import java.time.LocalDate;

import br.com.cdb.bancodigital.config.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.*;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

	private Long id;
	private String nome;
	private String cpf;
	private CategoriaCliente categoria;
	private LocalDate dataNascimento;
	private Usuario usuario;

}

