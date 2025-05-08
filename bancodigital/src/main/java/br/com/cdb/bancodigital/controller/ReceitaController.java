package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.ConsultaCpfDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;
import br.com.cdb.bancodigital.resttemplate.ReceitaFederalRestTemplate;

@RestController
@RequestMapping("/receita-federal")
@AllArgsConstructor
@Slf4j
public class ReceitaController {
	
	private final ReceitaFederalRestTemplate receitaFederalRestTemplate;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/consultar-cpf/")
	public ResponseEntity<CpfValidationResponse> consultarCpf(@Valid @RequestBody ConsultaCpfDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando consulta de CPF");

		CpfValidationResponse response = receitaFederalRestTemplate.consultarCpf(dto.getCpf());
		log.info("Consulta de CPF concluída com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Consulta de CPF concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}

}
