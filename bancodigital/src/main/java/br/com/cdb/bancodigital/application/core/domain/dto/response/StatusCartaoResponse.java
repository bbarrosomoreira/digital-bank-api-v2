package br.com.cdb.bancodigital.application.core.domain.dto.response;


import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.Status;

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
