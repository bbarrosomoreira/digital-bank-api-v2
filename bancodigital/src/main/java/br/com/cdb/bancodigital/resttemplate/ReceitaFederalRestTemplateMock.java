package br.com.cdb.bancodigital.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;

@Component
@Slf4j
@Profile("dev")  // Ativo apenas no perfil de desenvolvimento
public class ReceitaFederalRestTemplateMock implements ReceitaFederalRestTemplate {

	@Override
	public boolean isCpfInvalidoOuInativo(String cpf) {
		return false; // Sempre retorna como válido e ativo
	}
	
	@Override
	public CpfValidationResponse consultarCpf(String cpf) {
		log.info("Simulando consulta de CPF na Receita Federal");

		// Simulação de resposta da Receita Federal
		CpfValidationResponse response = new CpfValidationResponse();
		response.setSuccess(true);
		response.setCpf(cpf);
		response.setValid(true);
		response.setStatus("ATIVO");
		response.setMessage("CPF válido e ativo");
		return response; // Sempre retorna como válido e ativo 
	}

}
