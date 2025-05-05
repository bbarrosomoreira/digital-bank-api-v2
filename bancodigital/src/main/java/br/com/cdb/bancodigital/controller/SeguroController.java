package br.com.cdb.bancodigital.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import br.com.cdb.bancodigital.dto.AcionarSeguroFraudeDTO;
import br.com.cdb.bancodigital.dto.ContratarSeguroDTO;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.dto.response.AcionarSeguroFraudeResponse;
import br.com.cdb.bancodigital.dto.response.AcionarSeguroViagemResponse;
import br.com.cdb.bancodigital.dto.response.CancelarSeguroResponse;
import br.com.cdb.bancodigital.dto.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigital.dto.response.SeguroResponse;
import br.com.cdb.bancodigital.dto.response.TipoSeguroResponse;
import br.com.cdb.bancodigital.service.SeguroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/seguros")
@AllArgsConstructor
@Slf4j
public class SeguroController {
	
	private final SeguroService seguroService;
	
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping
	public ResponseEntity<SeguroResponse> contratarSeguro (
			@Valid @RequestBody ContratarSeguroDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando contratação de seguro.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		SeguroResponse response = seguroService.contratarSeguro(dto.getId_cartao(), usuarioLogado, dto.getTipo());
		log.info("Seguro contratado com sucesso.");
		long endTime = System.currentTimeMillis();
		log.info("Contratação de seguro concluída em {} ms.", endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

	// Listar tipos de Seguros Disponiveis (suas descricoes)
	@GetMapping("/tipos")
	public ResponseEntity<List<TipoSeguroResponse>> listarTiposSeguros(){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de tipos de seguros.");

		List<TipoSeguroResponse> tipos = Arrays.stream(TipoSeguro.values())
				.map(seguro -> new TipoSeguroResponse(seguro.getNome(), seguro.getDescricao(), seguro.getCondicoes()))
				.toList();
		log.info("Total de tipos de seguros encontrados: {}.", tipos.size());
		long endTime = System.currentTimeMillis();
		log.info("Busca de tipos de seguros concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(tipos);
	}
	
	// só admin pode puxar uma lista de todos seguros
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<SeguroResponse>> getSeguros(){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de todos os seguros.");

		List<SeguroResponse> seguros = seguroService.getSeguros();
		log.info("Total de seguros encontrados: {}.", seguros.size());
		long endTime = System.currentTimeMillis();
		log.info("Busca de seguros concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(seguros);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/cartao/{id_cartao}")
	public ResponseEntity<List<SeguroResponse>> listarPorCartao(
			@PathVariable Long id_cartao,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de seguros do cartão ID: {}.", id_cartao);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		List<SeguroResponse> seguros = seguroService.getSeguroByCartaoId(id_cartao, usuarioLogado);
		log.info("Total de seguros encontrados: {}.", seguros.size());
		long endTime = System.currentTimeMillis();
		log.info("Busca de seguro concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(seguros);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<SeguroResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de seguros do cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		List<SeguroResponse> seguros = seguroService.getSeguroByClienteId(id_cliente, usuarioLogado);
		log.info("Total de seguros encontrados: {}.", seguros.size());
		long endTime = System.currentTimeMillis();
		log.info("Busca de seguro concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(seguros);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/{id_seguro}")
	public ResponseEntity<SeguroResponse> getSeguroById(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de seguro ID: {}.", id_seguro);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		SeguroResponse seguro = seguroService.getSeguroById(id_seguro, usuarioLogado);
		log.info("Informações do seguro obtidas com sucesso.");
		long endTime = System.currentTimeMillis();
		log.info("Busca de seguro concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(seguro);	
	}
	
	// para usuário logado ver informações de seus cartoes (cliente)
	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("/meus-seguros")
	public ResponseEntity<List<SeguroResponse>> buscarSegurosDoUsuario (
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de seguros do usuário logado.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		List<SeguroResponse> seguros = seguroService.listarPorUsuario(usuarioLogado);
		log.info("Total de seguros encontrados: {}.", seguros.size());
		long endTime = System.currentTimeMillis();
		log.info("Busca de seguros concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(seguros);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping("/{id_seguro}/cancelar")
	public ResponseEntity<CancelarSeguroResponse> cancelarSeguro(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando cancelamento de seguro ID: {}.", id_seguro);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		CancelarSeguroResponse response = seguroService.cancelarSeguro(id_seguro, usuarioLogado);
		log.info("Seguro cancelado com sucesso.");
		long endTime = System.currentTimeMillis();
		log.info("Cancelamento de seguro concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// só o admin pode confirmar a exclusão de cadastro de seguros
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/cliente/{id_cliente}")
	public ResponseEntity<Void> deleteSegurosByCliente(
			@PathVariable Long id_cliente){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando exclusão de seguros do cliente ID: {}.", id_cliente);

		seguroService.deleteSegurosByCliente(id_cliente);
		log.info("Seguros do cliente ID: {} excluídos com sucesso.", id_cliente);
		long endTime = System.currentTimeMillis();
		log.info("Exclusão de seguros concluída em {} ms.", endTime - startTime);
		return ResponseEntity.noContent().build();
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping("/fraude/{id_seguro}/acionar")
	public ResponseEntity<AcionarSeguroFraudeResponse> acionarSeguroFraude(
			@PathVariable Long id_seguro,
			@Valid @RequestBody AcionarSeguroFraudeDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando acionamento de seguro de fraude.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		Seguro seguro = seguroService.acionarSeguro(id_seguro, usuarioLogado, dto.getValorFraude());
		log.info("Seguro de fraude acionado com sucesso.");
		long endTime = System.currentTimeMillis();
		log.info("Acionamento de seguro de fraude concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(AcionarSeguroFraudeResponse.toSeguroFraudeResponse(seguro));
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@PutMapping("/viagem/{id_seguro}/acionar")
	public ResponseEntity<AcionarSeguroViagemResponse> acionarSeguroViagem(
			@PathVariable Long id_seguro,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando acionamento de seguro de viagem.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		Seguro seguro = seguroService.acionarSeguro(id_seguro, usuarioLogado, BigDecimal.ZERO);
		log.info("Seguro de viagem acionado com sucesso.");
		long endTime = System.currentTimeMillis();
		log.info("Acionamento de seguro de viagem concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(AcionarSeguroViagemResponse.toSeguroViagemResponse(seguro));
	}
	
	// debitar premio seguro quando ativo
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/viagem/{id_seguro}/premio")
	public ResponseEntity<DebitarPremioSeguroResponse> debitarPremioSeguro(
			@PathVariable Long id_seguro){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando débito de prêmio de seguro.");

		DebitarPremioSeguroResponse response = seguroService.debitarPremioSeguro(id_seguro);
		log.info("Prêmio de seguro debitado com sucesso.");
		long endTime = System.currentTimeMillis();
		log.info("Débito de prêmio de seguro concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
		
	}
	
	
	
}
