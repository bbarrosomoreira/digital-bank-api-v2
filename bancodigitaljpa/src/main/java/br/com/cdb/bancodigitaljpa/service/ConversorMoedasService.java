package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cdb.bancodigitaljpa.enums.Moeda;

@Service
public class ConversorMoedasService {

	@Autowired
	private RestTemplate restTemplate;
	
	private final String API_URL = "https://api.apilayer.com/currency_data/convert";
    private final String API_KEY = "j6k9BY4OO4oXOOhLiUbdjPzfsKKcxoIZ";
	
	
	public BigDecimal converterParaBrl(Moeda moedaOrigem, BigDecimal valor) {
		if (Moeda.BRL == moedaOrigem) {
			return valor;
		}
		
		try {
			String url = API_URL + "?from=" + moedaOrigem + "&to=BRL&amount=" + valor;
			
			org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
			headers.set("apikey", API_KEY);
			
			org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
			
			org.springframework.http.ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    String.class
            );
			
			
			String jsonString = responseEntity.getBody();
			System.out.println("JSON recebido da API:");
			System.out.println(jsonString);
			
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonString);
            BigDecimal result = root.path("result").decimalValue();

            return result.setScale(2, RoundingMode.HALF_UP);
			
			
		} catch (Exception e) {
			System.out.println("Erro na conversão de moeda: " + e.getMessage());
			e.printStackTrace();
		}
		
		return BigDecimal.ZERO;
		
	}
}
