package br.com.cdb.bancodigitaljpa.dto.response;


import br.com.cdb.bancodigitaljpa.model.Cartao;
import br.com.cdb.bancodigitaljpa.model.enums.Status;

public record StatusCartaoResponse (
		String numCartao,
		Status statusNovo
		){
	public static StatusCartaoResponse toStatusCartaoResponse (Cartao cartao, Status statusNovo) {
		return new StatusCartaoResponse(
				cartao.getNumeroCartao(),
				statusNovo);
	}

}
