package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.utils.ConstantUtils;
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
	
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PostMapping("/cliente")
	public ResponseEntity<ClienteResponse> cadastrarCliente(
			@Valid @RequestBody ClienteUsuarioDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_CADASTRO_CLIENTE);

		ClienteResponse response = adminService.cadastrarCliente(dto);
		log.info(ConstantUtils.SUCESSO_CADASTRO_CLIENTE, response.getId());

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PostMapping("/conta/{id_cliente}")
	public ResponseEntity<ContaResponse> abrirConta(
			@PathVariable Long id_cliente,
			@Valid @RequestBody AbrirContaDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ABERTURA_CONTA, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		log.info(ConstantUtils.CLIENTE_ENCONTRADO);

		ContaResponse response = adminService.abrirConta(dto.getId_cliente(), usuarioCliente, dto.getTipoConta(), dto.getMoeda(), dto.getValorDeposito());
		log.info(ConstantUtils.SUCESSO_ABERTURA_CONTA, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PostMapping("/cartao/{id_cliente}")
	public ResponseEntity<CartaoResponse> emitirCartao(
			@PathVariable Long id_cliente,
			@Valid @RequestBody EmitirCartaoDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_EMISSAO_CARTAO, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		log.info(ConstantUtils.CLIENTE_ENCONTRADO);

		CartaoResponse response = adminService.emitirCartao(dto.getId_conta(), usuarioCliente, dto.getTipoCartao(), dto.getSenha());
		log.info(ConstantUtils.SUCESSO_EMISSAO_CARTAO, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PostMapping("/seguro/{id_cliente}")
	public ResponseEntity<SeguroResponse> contratarSeguro (
			@PathVariable Long id_cliente,
			@Valid @RequestBody ContratarSeguroDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_CONTRATACAO_SEGURO, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		Usuario usuarioCliente = adminService.getClienteById(id_cliente, usuarioLogado).getUsuario();
		log.info(ConstantUtils.CLIENTE_ENCONTRADO);

		SeguroResponse response = adminService.contratarSeguro(dto.getId_cartao(), usuarioCliente, dto.getTipo());
		log.info(ConstantUtils.SUCESSO_CONTRATACAO_SEGURO, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

}
