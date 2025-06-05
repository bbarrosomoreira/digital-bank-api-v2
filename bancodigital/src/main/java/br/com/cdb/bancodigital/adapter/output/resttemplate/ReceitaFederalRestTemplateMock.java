package br.com.cdb.bancodigital.adapter.output.resttemplate;

import br.com.cdb.bancodigital.application.port.out.api.ReceitaFederalPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CpfValidationResponse;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Component
@Slf4j
@Profile("dev")  // Ativo apenas no perfil de desenvolvimento
public class ReceitaFederalRestTemplateMock implements ReceitaFederalPort {

	@Override
	public boolean isCpfInvalidoOuInativo(String cpf) {
		return false; // Sempre retorna como válido e ativo
	}
	
	@Override
	public CpfValidationResponse consultarCpf(String cpf) {
		log.info(ConstantUtils.SIMULANDO_CONSULTA_CPF);

		// Simulação de resposta da Receita Federal
		CpfValidationResponse response = new CpfValidationResponse();
		response.setSuccess(true);
		response.setCpf(cpf);
		response.setValid(true);
		response.setStatus(ConstantUtils.STATUS_ATIVO);
		response.setMessage(ConstantUtils.CPF_VALIDO_ATIVO);
		return response; // Sempre retorna como válido e ativo 
	}

}

