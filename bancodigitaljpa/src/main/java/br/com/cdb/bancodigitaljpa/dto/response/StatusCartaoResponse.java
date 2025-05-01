package br.com.cdb.bancodigitaljpa.dto.response;


import br.com.cdb.bancodigitaljpa.model.CartaoBase;
import br.com.cdb.bancodigitaljpa.model.enums.Status;

public record StatusCartaoResponse (
		String numCartao,
		Status statusNovo
		){
	public static StatusCartaoResponse toStatusCartaoResponse (CartaoBase cartao, Status statusNovo) {
		return new StatusCartaoResponse(
				cartao.getNumeroCartao(),
				statusNovo);
	}

}
