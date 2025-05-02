package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import jakarta.validation.constraints.NotNull;
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
