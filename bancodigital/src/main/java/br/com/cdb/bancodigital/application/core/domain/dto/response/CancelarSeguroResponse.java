package br.com.cdb.bancodigital.application.core.domain.dto.response;

import br.com.cdb.bancodigital.application.core.domain.entity.Seguro;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.Status;

public record CancelarSeguroResponse (
		String tipoSeguro,
		String numApolice,
		Status status
		){
	public static CancelarSeguroResponse toCancelarSeguroResponse (Seguro seguro) {
		return new CancelarSeguroResponse(
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getStatusSeguro()
				);
	}

}