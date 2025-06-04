package br.com.cdb.bancodigital.application.core.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoSeguroResponse {
	private String nome;
	private String descricao;
	private String condicoes;
	
}
