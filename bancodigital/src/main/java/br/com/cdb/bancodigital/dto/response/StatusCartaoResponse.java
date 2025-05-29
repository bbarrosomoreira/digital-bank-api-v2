package br.com.cdb.bancodigital.dto.response;


import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.enums.Status;

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
