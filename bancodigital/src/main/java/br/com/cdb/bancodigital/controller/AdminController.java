package br.com.cdb.bancodigital.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.dto.AbrirContaDTO;
import br.com.cdb.bancodigital.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.dto.ContratarSeguroDTO;
import br.com.cdb.bancodigital.dto.EmitirCartaoDTO;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.dto.response.ContaResponse;
import br.com.cdb.bancodigital.dto.response.SeguroResponse;
import br.com.cdb.bancodigital.service.AdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
	
	private final AdminService adminService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/cliente")
	public ResponseEntity<ClienteResponse> cadastrarCliente(
			@Valid @RequestBody ClienteUsuarioDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando cadastro de cliente.");

		ClienteResponse response = adminService.cadastrarCliente(dto);
		log.info("Cliente cadastrado com sucesso: ID: {}", response.getId());

		long endTime = System.currentTimeMillis();
		log.info("Cadastro de cliente concluído em {} ms.", endTime - startTime);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/conta/{id_cliente}")
	public ResponseEntity<ContaResponse> abrirConta(
			@PathVariable Long id_cliente,
			@Valid @RequestBody AbrirContaDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando abertura de conta para cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		log.info("Cliente alvo encontrado");

		ContaResponse response = adminService.abrirConta(dto.getId_cliente(), usuarioCliente, dto.getTipoConta(), dto.getMoeda(), dto.getValorDeposito());
		log.info("Conta criada com sucesso para cliente ID: {}.", id_cliente);

		long endTime = System.currentTimeMillis();
		log.info("Abertura de conta concluída em {} ms.", endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/cartao/{id_cliente}")
	public ResponseEntity<CartaoResponse> emitirCartao(
			@PathVariable Long id_cliente,
			@Valid @RequestBody EmitirCartaoDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando emissão de cartão para cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		log.info("Cliente alvo encontrado");

		CartaoResponse response = adminService.emitirCartao(dto.getId_conta(), usuarioCliente, dto.getTipoCartao(), dto.getSenha());
		log.info("Cartão emitido com sucesso para cliente ID: {}.", id_cliente);

		long endTime = System.currentTimeMillis();
		log.info("Emissão de cartão concluída em {} ms.", endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/seguro/{id_cliente}")
	public ResponseEntity<SeguroResponse> contratarSeguro (
			@PathVariable Long id_cliente,
			@Valid @RequestBody ContratarSeguroDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando contratação de seguro para cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}", usuarioLogado.getId());

		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		log.info("Cliente alvo encontrado");

		SeguroResponse response = adminService.contratarSeguro(dto.getId_cartao(), usuarioCliente, dto.getTipo());
		log.info("Seguro contratado com sucesso para cliente ID: {}.", id_cliente);

		long endTime = System.currentTimeMillis();
		log.info("Contratação de seguro concluída em {} ms.", endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

}
