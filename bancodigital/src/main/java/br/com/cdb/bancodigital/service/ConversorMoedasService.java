package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import br.com.cdb.bancodigital.resttemplate.ConversorMoedasRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.dto.response.ApiConversorMoedasResponse;

@Service
@Slf4j
public class ConversorMoedasService {

	@Autowired
	private ConversorMoedasRestTemplate conversorMoedasRestTemplate;

	public Optional<ApiConversorMoedasResponse> fazerConversao(Moeda from, Moeda to, BigDecimal amount) {
		if (from == null || to == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			log.warn("Parâmetros inválidos para conversão de moeda.");
			return Optional.empty();
		}

		try {
			ApiConversorMoedasResponse resposta = conversorMoedasRestTemplate.chamarApiConversao(from, to, amount);
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
			ApiConversorMoedasResponse resposta = conversorMoedasRestTemplate.chamarApiConversao(Moeda.BRL, to, amount);
			if (resposta != null && resposta.getResult() != null) {
				return resposta.getResult().setScale(2, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			log.error("Erro na conversão de BRL para {}: {}", to, e.getMessage());
		}

		return BigDecimal.ZERO;
	}
}
