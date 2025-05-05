package br.com.cdb.bancodigital.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import br.com.brasilapi.BrasilAPI;
import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;

@Component
@Slf4j
public class BrasilApiRestTemplate {

	public CEP2 buscarEnderecoPorCep(String cep) {
        if (cep == null || cep.isBlank()) {
            log.error("CEP inválido: valor nulo ou vazio.");
            throw new ValidationException("CEP não pode ser nulo ou vazio.");
        }

		try {
            log.info("Buscando endereço");
			return BrasilAPI.cep2(cep);
		} catch (Exception e) {
			// Verifica se o erro veio da API
            if (isApiError(e)) {
                log.warn("Erro ao comunicar com a BrasilAPI. Usando fallback. Erro: {}", e.getMessage());
                return fallbackCep(cep);
            }
			
			log.error("CEP inválido ou erro inesperado", e);
			throw new ValidationException("CEP inválido ou erro inesperado");
		}
	}
    private boolean isApiError(Exception e) {
        return e.getMessage().contains("java.net") || e.getMessage().contains("503") || e.getCause() instanceof java.io.IOException;
    }
	
    // Fallback para quando a API não retorna o endereço
    private CEP2 fallbackCep(String cep) {
        log.info("Usando fallback para o CEP");
        CEP2 endereco = new CEP2();
        endereco.setCep(cep);
        endereco.setStreet("Rua não disponível");
        endereco.setNeighborhood("Bairro não disponível");
        endereco.setCity("Cidade não disponível");
        endereco.setState("Estado não disponível");
        return endereco;
    }

}
