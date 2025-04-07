package br.com.cdb.bancodigitaljpa.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.dto.AcionarSeguroDTO;
import br.com.cdb.bancodigitaljpa.dto.AcionarSeguroResponse;
import br.com.cdb.bancodigitaljpa.dto.ContratarSeguroDTO;
import br.com.cdb.bancodigitaljpa.dto.SeguroResponse;
import br.com.cdb.bancodigitaljpa.dto.TipoSeguroResponse;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import br.com.cdb.bancodigitaljpa.service.SeguroService;

@RestController
@RequestMapping("/seguros")
public class SeguroController {
	
	@Autowired
	private SeguroService seguroService;
	
	//contratar seguro
	@PostMapping("/add")
	public ResponseEntity<SeguroResponse> contratarSeguro (@RequestBody ContratarSeguroDTO dto) {
		SeguroResponse response = seguroService.contratarSeguro(dto.getId_cartao(), dto.getTipo());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

	// Listar tipos de Seguros Disponiveis (suas descricoes)
	@GetMapping("/tipos")
	public ResponseEntity<List<TipoSeguroResponse>> listarTiposSeguros(){
		List<TipoSeguroResponse> tipos = Arrays.stream(TipoSeguro.values())
				.map(seguro -> new TipoSeguroResponse(seguro.getNome(), seguro.getDescricao(), seguro.getCondicoes()))
				.toList();
		return ResponseEntity.ok(tipos);
	}
	
	// Listar todos seguros
	@GetMapping("/listAll")
	public ResponseEntity<List<SeguroResponse>> getSeguros(){
		List<SeguroResponse> seguros = seguroService.getSeguros();
		return ResponseEntity.ok(seguros);
	}
	
	// Listar Seguro por cartao
	@GetMapping("/cartao/{id_cartao}")
	public ResponseEntity<List<SeguroResponse>> listarPorCartao(
			@PathVariable Long id_cartao){
		List<SeguroResponse> seguros = seguroService.getSeguroByCartaoId(id_cartao);
		return ResponseEntity.ok(seguros);
	}
	
	// Listar Seguro por cliente
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<SeguroResponse>> listarPorCliente(
			@PathVariable Long id_cliente){
		List<SeguroResponse> seguros = seguroService.getSeguroByClienteId(id_cliente);
		return ResponseEntity.ok(seguros);
	}
	
	// Listar by id
	@GetMapping("/{id_seguro}")
	public ResponseEntity<SeguroResponse> getSeguroById(
			@PathVariable Long id_seguro){
		SeguroResponse response = seguroService.getSeguroById(id_seguro);
		return ResponseEntity.ok(response);	
	}
	
	// cancelar apólice
	@PutMapping("/{id_seguro}/cancelar")
	public ResponseEntity<String> cancelarSeguro(
			@PathVariable Long id_seguro){
		seguroService.cancelarSeguro(id_seguro);
		return ResponseEntity.ok("Apólice de seguro cancelada com sucesso!");
	}
	
	// acionar seguro
	@PutMapping("/fraude/{id_seguro}/acionar")
	public ResponseEntity<AcionarSeguroResponse> acionarSeguro(
			@PathVariable Long id_seguro,
			@RequestBody AcionarSeguroDTO dto){
		CartaoCredito ccr = (CartaoCredito) seguroService.getSeguroByCartaoId(dto.getId_cartao());
		AcionarSeguroResponse response = seguroService.acionarSeguroFraude(id_seguro,ccr, dto.getValorFraude());
		return ResponseEntity.ok(response);
	}
	
	// debitar premio seguro
	@PostMapping("/viagem/{id_seguro}/premio")
	public ResponseEntity<String> debitarPremioSeguroViagem(
			@PathVariable Long id_seguro){
		seguroService.debitarPremioSeguroViagem(id_seguro);
		return ResponseEntity.ok("Prêmio do Seguro Viagem debitado com sucesso!");
		
	}
	
	
	
}
