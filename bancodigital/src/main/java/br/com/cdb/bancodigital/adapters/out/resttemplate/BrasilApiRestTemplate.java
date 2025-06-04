package br.com.cdb.bancodigital.adapters.out.resttemplate;

import br.com.cdb.bancodigital.application.port.out.api.BrasilApiPort;
import br.com.cdb.bancodigital.exceptions.custom.CommunicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import br.com.brasilapi.BrasilAPI;
import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Component
@Slf4j
public class BrasilApiRestTemplate implements BrasilApiPort {

	public CEP2 buscarEnderecoPorCep(String cep) throws ValidationException {
        if (cep == null || cep.isBlank()) {
            log.error(ConstantUtils.CEP_INVALIDO);
            throw new ValidationException(ConstantUtils.CEP_OBRIGATORIO);
        }

		try {
            log.info(ConstantUtils.INICIO_BUSCA_ENDERECO);
			return BrasilAPI.cep2(cep);
		} catch (Exception e) {
			// Verifica se o erro veio da API
            if (isApiError(e)) {
                log.warn(ConstantUtils.ERRO_COMUNICACAO_BRASIL_API, e.getMessage());
                return fallbackCep(cep);
            }
			
			log.error(ConstantUtils.ERRO_CEP_INVALIDO, e);
			throw new CommunicationException(ConstantUtils.ERRO_CEP_INVALIDO);
		}
	}
    private boolean isApiError(Exception e) {
        return e.getMessage().contains("java.net") || e.getMessage().contains("503") || e.getCause() instanceof java.io.IOException;
    }
    // Fallback para quando a API não retorna o endereço
    private CEP2 fallbackCep(String cep) {
        log.info(ConstantUtils.INICIO_FALLBACK_CEP);
        CEP2 endereco = new CEP2();
        endereco.setCep(cep);
        endereco.setStreet(ConstantUtils.FALLBACK_RUA);
        endereco.setNeighborhood(ConstantUtils.FALLBACK_BAIRRO);
        endereco.setCity(ConstantUtils.FALLBACK_CIDADE);
        endereco.setState(ConstantUtils.FALLBACK_ESTADO);
        return endereco;
    }

}
