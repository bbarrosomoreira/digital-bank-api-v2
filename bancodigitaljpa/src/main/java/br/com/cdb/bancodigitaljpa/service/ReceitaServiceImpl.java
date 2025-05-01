package br.com.cdb.bancodigitaljpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cdb.bancodigitaljpa.dto.response.CpfValidationResponse;

@Service 
@Profile("prod") // roda apenas com a API externa de simulação da Receita Federal
public class ReceitaServiceImpl implements ReceitaService{
	
	private static final Logger log = LoggerFactory.getLogger(ReceitaServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;
	
	private final String API_URL = "http://localhost:8081/api/receita/cpf/";
	
	public CpfValidationResponse consultarCpf(String cpf) {
		
		try {
			String url = API_URL + cpf;
			ResponseEntity<CpfValidationResponse> response = 
					restTemplate.getForEntity(url, CpfValidationResponse.class);				
			return response.getBody();
			
		} catch (HttpClientErrorException e) { // Trata 4xx (ex: CPF inválido)
			log.warn("Erro ao consultar CPF: " + cpf + " - " + e.getStatusCode());
			return extrairCorpoDaMensagem(e.getResponseBodyAsString());
			
		} catch (HttpServerErrorException e) { // Trata 5xx (ex: instabilidade no sistema)
			log.error("Erro na API da Receita: " + e.getMessage());
			return extrairCorpoDaMensagem(e.getResponseBodyAsString());
			
		} catch (Exception e) {
			log.error("Erro inesperado ao consultar CPF na Receita: " + e.getMessage());
			return null;
		}

	}
	
	@Override
	public boolean isCpfValidoEAtivo(String cpf) {
		CpfValidationResponse response = consultarCpf(cpf);
		return response.isValid().equals(Boolean.TRUE) && response.getStatus().equals("ATIVO");
	}
	
	private CpfValidationResponse extrairCorpoDaMensagem(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, CpfValidationResponse.class);
		} catch (JsonProcessingException e) {
			log.error("Erro ao processar corpo da resposta de erro: " + e.getMessage());
		}
	    return new CpfValidationResponse();
	}
	
	
}
