package br.com.cdb.bancodigitaljpa.dto;


import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.enums.Status;

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
