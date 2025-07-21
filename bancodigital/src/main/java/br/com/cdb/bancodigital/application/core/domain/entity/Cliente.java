package br.com.cdb.bancodigital.application.core.domain.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;

import static br.com.cdb.bancodigital.utils.ConstantUtils.FORMATO_DATA_DD_MM_YYYY;


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

	@JsonFormat(pattern = FORMATO_DATA_DD_MM_YYYY)
	private LocalDate dataNascimento;
	private Usuario usuario;

}

