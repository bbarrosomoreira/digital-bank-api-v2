package br.com.cdb.bancodigitaljpa.dto;

import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
import br.com.cdb.bancodigitaljpa.enums.Status;

public record CancelarSeguroResponse (
		String tipoSeguro,
		String numApolice,
		Status status
		){
	public static CancelarSeguroResponse toCancelarSeguroResponse (SeguroBase seguro) {
		return new CancelarSeguroResponse(
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getStatusSeguro()
				);
	}

}