package br.com.cdb.bancodigitaljpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cdb.bancodigitaljpa.response.CpfValidationResponse;

@Service 
public class ReceitaCpfService {

	@Autowired
	private RestTemplate restTemplate;
	
	private final String URL_BASE = "http://localhost:8081/api/receita/cpf/";
	
	public CpfValidationResponse consultarCpf(String cpf) {
		String url = URL_BASE + cpf;
		try {
			ResponseEntity<CpfValidationResponse> response = restTemplate.getForEntity(url, CpfValidationResponse.class);				
			return response.getBody();	
		} catch (HttpClientErrorException e) {
			try {
				String body = e.getResponseBodyAsString();
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(body, CpfValidationResponse.class);
			} catch (Exception parseError) {
				System.out.println("Erro ao interpretar resposta 400: " + parseError.getMessage());
			}
			
		} catch (Exception e) {
			System.out.println("Erro ao consultar CPF na Receita: " + e.getMessage());
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
