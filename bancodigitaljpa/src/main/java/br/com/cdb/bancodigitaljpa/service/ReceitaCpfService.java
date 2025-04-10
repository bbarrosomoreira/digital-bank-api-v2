package br.com.cdb.bancodigitaljpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cdb.bancodigitaljpa.response.CpfValidationResponse;

@Service 
public class ReceitaCpfService {
	
	private static final Logger log = LoggerFactory.getLogger(ReceitaCpfService.class);

	@Autowired
	private RestTemplate restTemplate;
	
	private final String API_URL = "http://localhost:8081/api/receita/cpf/";
	
	public CpfValidationResponse consultarCpf(String cpf) {
		String url = API_URL + cpf;
		try {
			ResponseEntity<CpfValidationResponse> response = restTemplate.getForEntity(url, CpfValidationResponse.class);				
			return response.getBody();	
			
		} catch (HttpClientErrorException e) { // Trata 4xx (ex: CPF inválido)
			try {
				String body = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(body, CpfValidationResponse.class);
			} catch (Exception parseError) {
				log.error("Erro ao interpretar resposta 400: " + parseError.getMessage());
			}
			
		} catch (HttpServerErrorException e) { // Trata 5xx (ex: instabilidade no sistema)
			log.error("Erro 500 na API da Receita: " + e.getMessage());
		} catch (Exception e) {
			log.error("Erro genérico ao consultar CPF na Receita: " + e.getMessage());
		}
		
	    CpfValidationResponse fallback = new CpfValidationResponse();
	    fallback.setCpf(cpf);
	    fallback.setValid(false);
	    fallback.setStatus("INDEFINIDO");
	    fallback.setMessage("Erro na consulta externa (simulado)");
	    return fallback;
	
	}
	
	public boolean isCpfValidoEAtivo(String cpf) {
		CpfValidationResponse response = consultarCpf(cpf);
		return Boolean.TRUE.equals(response.getValid()) && "ATIVO".equals(response.getStatus());
	}
	
	
}
