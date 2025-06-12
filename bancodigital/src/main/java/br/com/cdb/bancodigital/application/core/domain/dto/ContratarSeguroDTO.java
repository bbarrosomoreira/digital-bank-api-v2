package br.com.cdb.bancodigital.application.core.domain.dto;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoSeguro;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContratarSeguroDTO {
	
	private Long id_cartao;

	private TipoSeguro tipo;

}
