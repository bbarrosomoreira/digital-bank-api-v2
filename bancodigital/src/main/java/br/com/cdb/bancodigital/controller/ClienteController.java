package br.com.cdb.bancodigital.controller;

import java.util.List;

import br.com.cdb.bancodigital.dto.ClienteAtualizadoDTO;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.dto.AtualizarCategoriaClienteDTO;
import br.com.cdb.bancodigital.dto.ClienteDTO;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ConstantUtils.CLIENTE)
@AllArgsConstructor
@Slf4j
public class ClienteController {

	private final ClienteService clienteService;

	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@PostMapping
	public ResponseEntity<ClienteResponse> cadastrarCliente(
			@Valid @RequestBody ClienteDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_CADASTRO_CLIENTE);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ClienteResponse response = clienteService.cadastrarCliente(dto, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_CADASTRO_CLIENTE, response.getId());

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// para usuário logado ver suas informações (cliente)
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@GetMapping(ConstantUtils.GET_USUARIO)
	public ResponseEntity<ClienteResponse> buscarClienteDoUsuario(
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ClienteResponse cliente = clienteService.buscarClienteDoUsuario(usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
	    return ResponseEntity.ok(cliente);
	}

	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@GetMapping
	public ResponseEntity<List<ClienteResponse>> getClientes() {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);

		List<ClienteResponse> clientes = clienteService.getClientes();
		log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(clientes);
	}

	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.CLIENTE_ID)
	public ResponseEntity<ClienteResponse> getClienteById(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE + ConstantUtils.ID_CLIENTE, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ClienteResponse cliente = clienteService.toResponse(clienteService.getClienteById(id_cliente, usuarioLogado));
		log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE + ConstantUtils.ID_CLIENTE, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(cliente);
	}
	
	// só o admin pode confirmar a exclusão de cadastro de cliente
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@DeleteMapping(ConstantUtils.CLIENTE_ID)
	public ResponseEntity<Void> deleteCliente(@PathVariable Long id_cliente){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_EXCLUSAO_CLIENTE, id_cliente);

		clienteService.deleteCliente(id_cliente);
		log.info(ConstantUtils.SUCESSO_EXCLUSAO_CLIENTE, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.noContent().build();
	}
	
	// admin podem atualizar dados cadastrais, cliente só se for dele
	@PutMapping(ConstantUtils.CLIENTE_ID)
	public ResponseEntity<ClienteResponse> updateCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody ClienteAtualizadoDTO clienteAtualizado,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ATUALIZACAO_CLIENTE, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ClienteResponse atualizado = clienteService.atualizarCliente(id_cliente, clienteAtualizado, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_ATUALIZACAO_CLIENTE, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(atualizado);
	}
	
	// admin podem atualizar dados cadastrais, cliente só se for dele
	@PatchMapping(ConstantUtils.CLIENTE_ID)
	public ResponseEntity<ClienteResponse> updateParcial(
			@PathVariable Long id_cliente,
			@Valid @RequestBody ClienteAtualizadoDTO clienteAtualizado,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ATUALIZACAO_CLIENTE, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ClienteResponse atualizado = clienteService.atualizarCliente(id_cliente, clienteAtualizado, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_ATUALIZACAO_CLIENTE, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(atualizado);
	}
	
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PatchMapping(ConstantUtils.CATEGORIA + ConstantUtils.CLIENTE_ID)
	public ResponseEntity<ClienteResponse> updateCategoriaCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody AtualizarCategoriaClienteDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ATUALIZACAO_CATEGORIA_CLIENTE, id_cliente);
		
		ClienteResponse atualizado = clienteService.updateCategoriaCliente(id_cliente, dto.getCategoriaCliente());
		log.info(ConstantUtils.SUCESSO_ATUALIZACAO_CATEGORIA_CLIENTE);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(atualizado);
	}
	
}
