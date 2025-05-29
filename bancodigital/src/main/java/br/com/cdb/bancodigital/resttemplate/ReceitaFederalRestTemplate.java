package br.com.cdb.bancodigital.resttemplate;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;

public interface ReceitaFederalRestTemplate {
	boolean isCpfInvalidoOuInativo(String cpf);
	CpfValidationResponse consultarCpf(String cpf);

}
