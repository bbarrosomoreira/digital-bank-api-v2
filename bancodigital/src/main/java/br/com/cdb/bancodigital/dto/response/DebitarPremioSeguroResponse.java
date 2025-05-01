package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.model.Seguro;

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