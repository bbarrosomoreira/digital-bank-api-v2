package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.response.ApiConversorMoedasResponse;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class ConversorMoedasService {
	
	private static final Logger log = LoggerFactory.getLogger(ConversorMoedasService.class);

	@Autowired
	private RestTemplate restTemplate;
	
	private static final Dotenv dotenv = Dotenv.load();
	
	private static final String API_URL = "https://api.apilayer.com/currency_data/convert";
    private static final String API_KEY = dotenv.get("API_KEY");
    
    public ApiConversorMoedasResponse fazerConversao(Moeda from, Moeda to, BigDecimal amount) {
    	try {
        	// Montar URL
        	String url = API_URL + "?from=" + from + "&to=" + to + "&amount=" + amount;
        	
        	// Montar headers (crachá)
        	HttpHeaders headers = new HttpHeaders();
        	headers.set("apikey", API_KEY);
        	HttpEntity<String> entity = new HttpEntity<>(headers);
        	
        	// Fazer requisição
        	ResponseEntity<ApiConversorMoedasResponse> response = restTemplate.exchange(
        			url,
        			HttpMethod.GET,
        			entity,
        			ApiConversorMoedasResponse.class);

        	// Retornar o body	
        	return response.getBody();
    	} catch (Exception e) {
			log.error("Erro na conversão de moeda: " + e.getMessage());
		}
		return null;
    }
	
    public BigDecimal converterDeBrl(Moeda to, BigDecimal amount) {
		if (Moeda.BRL == to) {
			return amount;
		}
    	try {
        	// Montar URL
        	String url = API_URL + "?from=" + "BRL" + "&to=" + to + "&amount=" + amount;
        	
        	// Montar headers (crachá)
        	HttpHeaders headers = new HttpHeaders();
        	headers.set("apikey", API_KEY);
        	HttpEntity<String> entity = new HttpEntity<>(headers);
        	
        	// Criar RestTemplate
        	RestTemplate restTemplate = new RestTemplate();
        	
        	// Fazer requisição
        	ResponseEntity<ApiConversorMoedasResponse> response = restTemplate.exchange(
        			url,
        			HttpMethod.GET,
        			entity,
        			ApiConversorMoedasResponse.class);

        	// Retornar o result do body	
        	return response.getBody().getResult().setScale(2, RoundingMode.HALF_UP);
    		
    	} catch (Exception e) {
			log.error("Erro na conversão de moeda: " + e.getMessage());
		}
		
		return BigDecimal.ZERO;
    }
}
