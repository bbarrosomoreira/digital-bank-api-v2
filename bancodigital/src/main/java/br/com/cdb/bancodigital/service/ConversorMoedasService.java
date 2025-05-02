package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.dto.response.ApiConversorMoedasResponse;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class ConversorMoedasService {

	private static final Logger log = LoggerFactory.getLogger(ConversorMoedasService.class);

	@Autowired
	private RestTemplate restTemplate;

	private static final String API_URL = "https://api.apilayer.com/currency_data/convert";

	@Value("${api.currency.key}")
	private String apiKey;

	public Optional<ApiConversorMoedasResponse> fazerConversao(Moeda from, Moeda to, BigDecimal amount) {
		if (from == null || to == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			log.warn("Parâmetros inválidos para conversão de moeda.");
			return Optional.empty();
		}

		try {
			ApiConversorMoedasResponse resposta = chamarApiConversao(from, to, amount);
			return Optional.ofNullable(resposta);
		} catch (Exception e) {
			log.error("Erro na conversão de moeda: {}", e.getMessage());
			return Optional.empty();
		}
	}

	public BigDecimal converterDeBrl(Moeda to, BigDecimal amount) {
		if (to == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			log.warn("Parâmetros inválidos para conversão de BRL.");
			return BigDecimal.ZERO;
		}

		if (to == Moeda.BRL) {
			return amount.setScale(2, RoundingMode.HALF_UP);
		}

		try {
			ApiConversorMoedasResponse resposta = chamarApiConversao(Moeda.BRL, to, amount);
			if (resposta != null && resposta.getResult() != null) {
				return resposta.getResult().setScale(2, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			log.error("Erro na conversão de BRL para {}: {}", to, e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	private ApiConversorMoedasResponse chamarApiConversao(Moeda from, Moeda to, BigDecimal amount) {
		String url = String.format("%s?from=%s&to=%s&amount=%s", API_URL, from, to, amount);

		HttpHeaders headers = new HttpHeaders();
		headers.set("apikey", apiKey);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<ApiConversorMoedasResponse> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				ApiConversorMoedasResponse.class
		);

		return response.getBody();
	}
}
