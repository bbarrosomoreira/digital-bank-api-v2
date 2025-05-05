package br.com.cdb.bancodigital.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;
import br.com.cdb.bancodigital.resttemplate.ReceitaFederalRestTemplate;

@RestController
@RequestMapping("/receita-federal")
@AllArgsConstructor
@Slf4j
public class ReceitaController {
	
	private final ReceitaFederalRestTemplate receitaFederalRestTemplate;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/consultar-cpf/{cpf}")
	public ResponseEntity<CpfValidationResponse> consultarCpf(@PathVariable String cpf) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando consulta de CPF");

		CpfValidationResponse response = receitaFederalRestTemplate.consultarCpf(cpf);
		log.info("Consulta de CPF concluída com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Consulta de CPF concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}

}
