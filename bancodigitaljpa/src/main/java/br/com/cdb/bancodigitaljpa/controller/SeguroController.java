package br.com.cdb.bancodigitaljpa.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.dto.AcionarSeguroFraudeDTO;
import br.com.cdb.bancodigitaljpa.dto.ContratarSeguroDTO;
import br.com.cdb.bancodigitaljpa.model.Seguro;
import br.com.cdb.bancodigitaljpa.model.Usuario;
import br.com.cdb.bancodigitaljpa.model.enums.TipoSeguro;
import br.com.cdb.bancodigitaljpa.dto.response.AcionarSeguroFraudeResponse;
import br.com.cdb.bancodigitaljpa.dto.response.AcionarSeguroViagemResponse;
import br.com.cdb.bancodigitaljpa.dto.response.CancelarSeguroResponse;
import br.com.cdb.bancodigitaljpa.dto.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigitaljpa.dto.response.SeguroResponse;
import br.com.cdb.bancodigitaljpa.dto.response.TipoSeguroResponse;
import br.com.cdb.bancodigitaljpa.service.SeguroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/seguros")
public class SeguroController {
	
	@Autowired
	private SeguroService seguroService;
	
	// ambos podem contratar novo seguro
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping
	public ResponseEntity<SeguroResponse> contratarSeguro (
			@Valid @RequestBody ContratarSeguroDTO dto,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		SeguroResponse response = seguroService.contratarSeguro(dto.getId_cartao(), usuarioLogado, dto.getTipo());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

	// cliente e admin
	// Listar tipos de Seguros Disponiveis (suas descricoes)
	@GetMapping("/tipos")
	public ResponseEntity<List<TipoSeguroResponse>> listarTiposSeguros(){
		List<TipoSeguroResponse> tipos = Arrays.stream(TipoSeguro.values())
				.map(seguro -> new TipoSeguroResponse(seguro.getNome(), seguro.getDescricao(), seguro.getCondicoes()))
				.toList();
		return ResponseEntity.ok(tipos);
	}
	
	// Listar todos seguros
	// só admin pode puxar uma lista de todos seguros
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<SeguroResponse>> getSeguros(){
		List<SeguroResponse> seguros = seguroService.getSeguros();
		return ResponseEntity.ok(seguros);
	}
	
	// Listar Seguro por cartao
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/cartao/{id_cartao}")
	public ResponseEntity<List<SeguroResponse>> listarPorCartao(
			@PathVariable Long id_cartao,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		List<SeguroResponse> seguros = seguroService.getSeguroByCartaoId(id_cartao, usuarioLogado);
		return ResponseEntity.ok(seguros);
	}
	
	// Listar Seguro por cliente
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<SeguroResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		List<SeguroResponse> seguros = seguroService.getSeguroByClienteId(id_cliente, usuarioLogado);
		return ResponseEntity.ok(seguros);
	}
	
	// Listar by id
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/{id_seguro}")
	public ResponseEntity<SeguroResponse> getSeguroById(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		SeguroResponse seguro = seguroService.getSeguroById(id_seguro, usuarioLogado);
		return ResponseEntity.ok(seguro);	
	}
	
	// para usuário logado ver informações de seus cartoes (cliente)
	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("/meus-seguros")
	public ResponseEntity<List<SeguroResponse>> buscarSegurosDoUsuario (
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		List<SeguroResponse> seguros = seguroService.listarPorUsuario(usuarioLogado);
		return ResponseEntity.ok(seguros);
	}
	
	// cancelar apólice
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping("/{id_seguro}/cancelar")
	public ResponseEntity<CancelarSeguroResponse> cancelarSeguro(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		CancelarSeguroResponse response = seguroService.cancelarSeguro(id_seguro, usuarioLogado);
		return ResponseEntity.ok(response);
	}
	
	// deletar seguros by cliente
	// só o admin pode confirmar a exclusão de cadastro de seguros
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/cliente/{id_cliente}")
	public ResponseEntity<Void> deleteSegurosByCliente(
			@PathVariable Long id_cliente){
		seguroService.deleteSegurosByCliente(id_cliente);
		return ResponseEntity.noContent().build();
	}
	
	// acionar seguro
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping("/fraude/{id_seguro}/acionar")
	public ResponseEntity<AcionarSeguroFraudeResponse> acionarSeguroFraude(
			@PathVariable Long id_seguro,
			@Valid @RequestBody AcionarSeguroFraudeDTO dto,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Seguro seguro = seguroService.acionarSeguro(id_seguro, usuarioLogado, dto.getValorFraude());
		return ResponseEntity.ok(AcionarSeguroFraudeResponse.toSeguroFraudeResponse(seguro);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping("/viagem/{id_seguro}/acionar")
	public ResponseEntity<AcionarSeguroViagemResponse> acionarSeguroViagem(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Seguro seguro = seguroService.acionarSeguro(id_seguro, usuarioLogado, BigDecimal.ZERO);
		return ResponseEntity.ok(AcionarSeguroViagemResponse.toSeguroViagemResponse(seguro);
	}
	
	// debitar premio seguro
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/viagem/{id_seguro}/premio")
	public ResponseEntity<DebitarPremioSeguroResponse> debitarPremioSeguro(
			@PathVariable Long id_seguro){
		DebitarPremioSeguroResponse response = seguroService.debitarPremioSeguro(id_seguro);
		return ResponseEntity.ok(response);
		
	}
	
	
	
}
