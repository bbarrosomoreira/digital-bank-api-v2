package br.com.cdb.bancodigitaljpa.controller;

import java.math.BigDecimal;
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

import br.com.cdb.bancodigitaljpa.dto.AcionarSeguroFraudeDTO;
import br.com.cdb.bancodigitaljpa.dto.ContratarSeguroDTO;
import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
import br.com.cdb.bancodigitaljpa.entity.SeguroFraude;
import br.com.cdb.bancodigitaljpa.entity.SeguroViagem;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import br.com.cdb.bancodigitaljpa.response.AcionarSeguroFraudeResponse;
import br.com.cdb.bancodigitaljpa.response.AcionarSeguroViagemResponse;
import br.com.cdb.bancodigitaljpa.response.CancelarSeguroResponse;
import br.com.cdb.bancodigitaljpa.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigitaljpa.response.SeguroResponse;
import br.com.cdb.bancodigitaljpa.response.TipoSeguroResponse;
import br.com.cdb.bancodigitaljpa.service.SeguroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/seguros")
public class SeguroController {
	
	@Autowired
	private SeguroService seguroService;
	
	//contratar seguro
	@PostMapping
	public ResponseEntity<SeguroResponse> contratarSeguro (@Valid @RequestBody ContratarSeguroDTO dto) {
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
	@GetMapping
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
		SeguroResponse seguro = seguroService.getSeguroById(id_seguro);
		return ResponseEntity.ok(seguro);	
	}
	
	// cancelar apólice
	@PutMapping("/{id_seguro}/cancelar")
	public ResponseEntity<CancelarSeguroResponse> cancelarSeguro(
			@PathVariable Long id_seguro){
		CancelarSeguroResponse response = seguroService.cancelarSeguro(id_seguro);
		return ResponseEntity.ok(response);
	}
	
	// acionar seguro
	@PutMapping("/fraude/{id_seguro}/acionar")
	public ResponseEntity<AcionarSeguroFraudeResponse> acionarSeguroFraude(
			@PathVariable Long id_seguro,
			@Valid @RequestBody AcionarSeguroFraudeDTO dto){
		SeguroBase seguro = seguroService.acionarSeguro(id_seguro, dto.getValorFraude());
		return ResponseEntity.ok(AcionarSeguroFraudeResponse.toSeguroFraudeResponse((SeguroFraude) seguro));
	}
	
	@PutMapping("/viagem/{id_seguro}/acionar")
	public ResponseEntity<AcionarSeguroViagemResponse> acionarSeguroFraude(
			@PathVariable Long id_seguro){
		SeguroBase seguro = seguroService.acionarSeguro(id_seguro, BigDecimal.ZERO);
		return ResponseEntity.ok(AcionarSeguroViagemResponse.toSeguroViagemResponse((SeguroViagem) seguro));
	}
	
	// debitar premio seguro
	@PostMapping("/viagem/{id_seguro}/premio")
	public ResponseEntity<DebitarPremioSeguroResponse> debitarPremioSeguro(
			@PathVariable Long id_seguro){
		DebitarPremioSeguroResponse response = seguroService.debitarPremioSeguro(id_seguro);
		return ResponseEntity.ok(response);
		
	}
	
	
	
}
