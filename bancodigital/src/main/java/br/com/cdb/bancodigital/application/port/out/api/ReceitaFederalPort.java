package br.com.cdb.bancodigital.application.port.out.api;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CpfValidationResponse;

public interface ReceitaFederalPort {
	boolean isCpfInvalidoOuInativo(String cpf);
	CpfValidationResponse consultarCpf(String cpf);

}
