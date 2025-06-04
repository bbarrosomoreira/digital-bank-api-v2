package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import br.com.cdb.bancodigital.adapters.out.resttemplate.ConversorMoedasRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.application.core.domain.model.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ApiConversorMoedasResponse;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Service
@Slf4j
public class ConversorMoedasService {

	@Autowired
	private ConversorMoedasRestTemplate conversorMoedasRestTemplate;

	public Optional<ApiConversorMoedasResponse> fazerConversao(Moeda from, Moeda to, BigDecimal amount) {
		if (from == null || to == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			log.warn(ConstantUtils.ERRO_CONVERSAO);
			return Optional.empty();
		}

		try {
			ApiConversorMoedasResponse resposta = conversorMoedasRestTemplate.converterMoeda(from, to, amount);
			return Optional.ofNullable(resposta);
		} catch (Exception e) {
			log.error(ConstantUtils.ERRO_CONVERSAO + " {}", e.getMessage());
			return Optional.empty();
		}
	}

	public BigDecimal converterDeBrl(Moeda to, BigDecimal amount) {
		if (to == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			log.warn(ConstantUtils.ERRO_CONVERSAO);
			return BigDecimal.ZERO;
		}

		if (to == Moeda.BRL) {
			return amount.setScale(2, RoundingMode.HALF_UP);
		}

		try {
			ApiConversorMoedasResponse resposta = conversorMoedasRestTemplate.converterMoeda(Moeda.BRL, to, amount);
			if (resposta != null && resposta.getResult() != null) {
				return resposta.getResult().setScale(2, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			log.error(ConstantUtils.ERRO_CONVERSAO + " {}", e.getMessage());
		}

		return BigDecimal.ZERO;
	}
}
