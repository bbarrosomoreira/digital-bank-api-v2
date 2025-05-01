package br.com.cdb.bancodigitaljpa.dto.response;

import br.com.cdb.bancodigitaljpa.model.SeguroBase;
import br.com.cdb.bancodigitaljpa.model.enums.Status;

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