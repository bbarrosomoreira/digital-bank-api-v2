package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.application.core.domain.entity.Seguro;

public record DebitarPremioSeguroResponse (
		String tipoSeguro,
		String numApolice,
		BigDecimal premio
		){
	public static DebitarPremioSeguroResponse toDebitarPremioSeguroResponse (Seguro seguro) {
		return new DebitarPremioSeguroResponse(
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getPremioApolice()
				);
	}

}