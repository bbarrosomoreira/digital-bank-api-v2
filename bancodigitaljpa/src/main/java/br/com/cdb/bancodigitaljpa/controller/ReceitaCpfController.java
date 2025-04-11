package br.com.cdb.bancodigitaljpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.response.CpfValidationResponse;
import br.com.cdb.bancodigitaljpa.service.ReceitaCpfService;

@RestController
@RequestMapping("/receita-federal")
public class ReceitaCpfController {
	
	@Autowired
	private ReceitaCpfService receitaCpfService;
	
	@GetMapping("/consultar-cpf/{cpf}")
	public ResponseEntity<CpfValidationResponse> consultarCpf(@PathVariable String cpf) {
		CpfValidationResponse response = receitaCpfService.consultarCpf(cpf);

		if(response.getValid().equals(Boolean.FALSE)) {
			return ResponseEntity.badRequest().body(response);
		} 
		else if (response == null || response.getValid() == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
		}
		
		return ResponseEntity.ok(response);
	}

}
