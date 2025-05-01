package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;

public interface ReceitaService {
	boolean isCpfValidoEAtivo(String cpf);
	CpfValidationResponse consultarCpf(String cpf);

}
