package br.com.cdb.bancodigital.controller;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.application.core.domain.dto.ConversorMoedasDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ApiConversorMoedasResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ConversorMoedasResponse;
import br.com.cdb.bancodigital.service.ConversorMoedasService;

@RestController
@RequestMapping(ConstantUtils.CAMBIO)
@AllArgsConstructor
@Slf4j
public class ConversorMoedasController {
	
	private final ConversorMoedasService conversorMoedasService;
	
	@GetMapping(ConstantUtils.CONVERSOR_REAL)
	public ResponseEntity<ConversorMoedasResponse> converterParaBrl (
			@RequestBody ConversorMoedasDTO dto){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_CONVERSAO);

		BigDecimal valorConvertido = conversorMoedasService.converterDeBrl(dto.getMoedaDestino(), dto.getValor());
		ConversorMoedasResponse response = new ConversorMoedasResponse();
		response.setMoeda(dto.getMoedaDestino());
		response.setValorOriginal(dto.getValor());
		response.setValorConvertido(valorConvertido);

		log.info(ConstantUtils.SUCESSO_CONVERSAO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(ConstantUtils.CONVERSOR_MOEDAS)
	public ResponseEntity<ApiConversorMoedasResponse> fazerConversao (
			@RequestBody ConversorMoedasDTO dto){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_CONVERSAO);

		return conversorMoedasService.fazerConversao(dto.getMoedaOrigem(), dto.getMoedaDestino(), dto.getValor())
				.map(response -> {
					log.info(ConstantUtils.SUCESSO_CONVERSAO);
					long endTime = System.currentTimeMillis();
					log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
					return ResponseEntity.ok(response);
				})
				.orElseGet(() -> {
					log.warn(ConstantUtils.ERRO_CONVERSAO);
					long endTime = System.currentTimeMillis();
					log.info(ConstantUtils.FIM_TENTATIVA_CHAMADA, endTime - startTime);
					return ResponseEntity.badRequest().build();
				});
	}

}
