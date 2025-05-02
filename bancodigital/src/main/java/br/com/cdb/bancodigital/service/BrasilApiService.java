package br.com.cdb.bancodigital.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.brasilapi.BrasilAPI;
import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;

@Service
public class BrasilApiService {
	
	private static final Logger log = LoggerFactory.getLogger(BrasilApiService.class);
	
	public CEP2 buscarEnderecoPorCep(String cep) {
		try {
			return BrasilAPI.cep2(cep);
		} catch (Exception e) {
			
			// Verifica se o erro veio da API (erro de rede, servidor etc.)
            if (e.getMessage().contains("java.net") || e.getMessage().contains("503") || e.getCause() instanceof java.io.IOException) {
                log.warn("API BrasilAPI indisponível. Usando fallback para o CEP: {}", cep);
                return fallbackCep(cep);
            }
			
			log.error("CEP inválido: {}", cep, e);
			throw new ValidationException("CEP inválido: " + cep);
		}
	}
	
    // Fallback simples
    private CEP2 fallbackCep(String cep) {
        CEP2 endereco = new CEP2();
        endereco.setCep(cep);
        endereco.setStreet("Rua não disponível");
        endereco.setNeighborhood("Bairro não disponível");
        endereco.setCity("Cidade não disponível");
        endereco.setState("Estado não disponível");
        return endereco;
    }

}
