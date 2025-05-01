package br.com.cdb.bancodigital.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;

@Service
@Profile("dev")
public class ReceitaMockService implements ReceitaService {

	@Override
	public boolean isCpfValidoEAtivo(String cpf) {
		return true; // Sempre retorna como válido e ativo 
	}
	
	@Override
	public CpfValidationResponse consultarCpf(String cpf) {
		CpfValidationResponse response = new CpfValidationResponse();
		response.setSuccess(true);
		response.setCpf(cpf);
		response.setValid(true);
		response.setStatus("ATIVO");
		response.setMessage("CPF válido e ativo");
		return response; // Sempre retorna como válido e ativo 
	}

}
