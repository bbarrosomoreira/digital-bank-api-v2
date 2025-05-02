package br.com.cdb.bancodigital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;
import br.com.cdb.bancodigital.service.ReceitaService;

@RestController
@RequestMapping("/receita-federal")
@RequiredArgsConstructor
public class ReceitaController {
	
	private final ReceitaService receitaService;
	
	// só admin pode verificar cpfs
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/consultar-cpf/{cpf}")
	public ResponseEntity<CpfValidationResponse> consultarCpf(@PathVariable String cpf) {
		CpfValidationResponse response = receitaService.consultarCpf(cpf);
		
		if (response == null || Boolean.FALSE.equals(response.isSuccess())) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
		}
		
		if (Boolean.FALSE.equals(response.isValid())) {
			return ResponseEntity.badRequest().body(response);
		} 
		
		return ResponseEntity.ok(response);
	}

}
