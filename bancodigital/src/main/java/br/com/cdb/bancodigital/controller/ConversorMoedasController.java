package br.com.cdb.bancodigital.controller;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.dto.ConversorMoedasDTO;
import br.com.cdb.bancodigital.dto.response.ApiConversorMoedasResponse;
import br.com.cdb.bancodigital.dto.response.ConversorMoedasResponse;
import br.com.cdb.bancodigital.service.ConversorMoedasService;

@RestController
@RequestMapping("/cambio")
@AllArgsConstructor
@Slf4j
public class ConversorMoedasController {
	
	private final ConversorMoedasService conversorMoedasService;
	
	@GetMapping("/conversor-real")
	public ResponseEntity<ConversorMoedasResponse> converterParaBrl (
			@RequestBody ConversorMoedasDTO dto){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando conversão de valor para BRL. Moeda de destino: {}.", dto.getMoedaDestino());

		BigDecimal valorConvertido = conversorMoedasService.converterDeBrl(dto.getMoedaDestino(), dto.getValor());
		ConversorMoedasResponse response = new ConversorMoedasResponse();
		response.setMoeda(dto.getMoedaDestino());
		response.setValorOriginal(dto.getValor());
		response.setValorConvertido(valorConvertido);

		log.info("Conversão concluída com sucesso. Valor original: {}, Valor convertido: {}.", dto.getValor(), valorConvertido);

		long endTime = System.currentTimeMillis();
		log.info("Conversão para BRL concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/conversor-moedas")
	public ResponseEntity<ApiConversorMoedasResponse> fazerConversao (
			@RequestBody ConversorMoedasDTO dto){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando conversão de moedas. Moeda origem: {}, Moeda destino: {}, Valor: {}.",
				dto.getMoedaOrigem(), dto.getMoedaDestino(), dto.getValor());

		return conversorMoedasService.fazerConversao(dto.getMoedaOrigem(), dto.getMoedaDestino(), dto.getValor())
				.map(response -> {
					log.info("Conversão realizada com sucesso.");
					long endTime = System.currentTimeMillis();
					log.info("Conversão de moedas concluída em {} ms.", endTime - startTime);
					return ResponseEntity.ok(response);
				})
				.orElseGet(() -> {
					log.warn("Conversão de moedas falhou. Verifique os parâmetros fornecidos.");
					long endTime = System.currentTimeMillis();
					log.info("Tentativa de conversão de moedas concluída em {} ms.", endTime - startTime);
					return ResponseEntity.badRequest().build();
				});
	}

}
