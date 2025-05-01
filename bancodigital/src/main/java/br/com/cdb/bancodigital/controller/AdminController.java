package br.com.cdb.bancodigital.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/cliente")
	public ResponseEntity<ClienteResponse> cadastrarCliente(
			@Valid @RequestBody ClienteUsuarioDTO dto,
			Authentication authentication) {
		ClienteResponse response = adminService.cadastrarCliente(dto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/conta/{id_cliente}")
	public ResponseEntity<ContaResponse> abrirConta(
			@PathVariable Long id_cliente,
			@Valid @RequestBody AbrirContaDTO dto,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		ContaResponse response = adminService.abrirConta(dto.getId_cliente(), usuarioCliente, dto.getTipoConta(), dto.getMoeda(), dto.getValorDeposito());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);	
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/cartao/{id_cliente}")
	public ResponseEntity<CartaoResponse> emitirCartao(
			@PathVariable Long id_cliente,
			@Valid @RequestBody EmitirCartaoDTO dto,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		CartaoResponse response = adminService.emitirCartao(dto.getId_conta(), usuarioCliente, dto.getTipoCartao(), dto.getSenha());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/seguro/{id_cliente}")
	public ResponseEntity<SeguroResponse> contratarSeguro (
			@PathVariable Long id_cliente,
			@Valid @RequestBody ContratarSeguroDTO dto,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		SeguroResponse response = adminService.contratarSeguro(dto.getId_cartao(), usuarioCliente, dto.getTipo());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

}
