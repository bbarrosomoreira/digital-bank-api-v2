package br.com.cdb.bancodigital.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
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
public class ConversorMoedasController {
	
	@Autowired
	private ConversorMoedasService conversorMoedasService;
	
	@GetMapping("/conversor-real")
	public ResponseEntity<ConversorMoedasResponse> converterParaBrl (
			@RequestBody ConversorMoedasDTO dto){
		BigDecimal valorConvertido = conversorMoedasService.converterDeBrl(dto.getMoedaDestino(), dto.getValor());
		ConversorMoedasResponse response = new ConversorMoedasResponse();
		response.setMoeda(dto.getMoedaDestino());
		response.setValorOriginal(dto.getValor());
		response.setValorConvertido(valorConvertido);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/conversor-moedas")
	public ResponseEntity<ApiConversorMoedasResponse> fazerConversao (
			@RequestBody ConversorMoedasDTO dto){
		ApiConversorMoedasResponse response = conversorMoedasService.fazerConversao(dto.getMoedaOrigem(), dto.getMoedaDestino(), dto.getValor());
		return ResponseEntity.ok(response);
	}

}
