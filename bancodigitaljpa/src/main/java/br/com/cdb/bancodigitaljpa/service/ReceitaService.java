package br.com.cdb.bancodigitaljpa.service;

import br.com.cdb.bancodigitaljpa.dto.response.CpfValidationResponse;

public interface ReceitaService {
	boolean isCpfValidoEAtivo(String cpf);
	CpfValidationResponse consultarCpf(String cpf);

}
