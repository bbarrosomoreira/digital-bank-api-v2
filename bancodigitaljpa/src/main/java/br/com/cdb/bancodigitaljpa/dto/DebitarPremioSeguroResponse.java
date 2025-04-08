package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.SeguroBase;

public record DebitarPremioSeguroResponse (
		String tipoSeguro,
		String numApolice,
		BigDecimal premio
		){
	public static DebitarPremioSeguroResponse toDebitarPremioSeguroResponse (SeguroBase seguro) {
		return new DebitarPremioSeguroResponse(
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getPremioApolice()
				);
	}

}