package br.com.cdb.bancodigital.application.core.domain.dto;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlterarStatusCartaoDTO {
	
	private Status status;

}
