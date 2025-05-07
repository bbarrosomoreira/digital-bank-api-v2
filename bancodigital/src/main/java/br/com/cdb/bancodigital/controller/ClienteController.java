package br.com.cdb.bancodigital.controller;

import java.util.List;

import br.com.cdb.bancodigital.dto.ClienteAtualizadoDTO;
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
@RequestMapping("/clientes")
@AllArgsConstructor
@Slf4j
public class ClienteController {

	private final ClienteService clienteService;

	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping
	public ResponseEntity<ClienteResponse> cadastrarCliente(
			@Valid @RequestBody ClienteDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando cadastro de cliente.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}.", usuarioLogado.getId());

		ClienteResponse response = clienteService.cadastrarCliente(dto, usuarioLogado);
		log.info("Cliente cadastrado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Cadastro de cliente concluído em {} ms.", endTime - startTime);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// para usuário logado ver suas informações (cliente)
	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("/me")
	public ResponseEntity<ClienteResponse> buscarClienteDoUsuario(
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Buscando informações do cliente logado.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}.", usuarioLogado.getId());

		ClienteResponse cliente = clienteService.buscarClienteDoUsuario(usuarioLogado);
		log.info("Informações do cliente logado obtidas com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Busca de informações do cliente logado concluída em {} ms.", endTime - startTime);
	    return ResponseEntity.ok(cliente);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<ClienteResponse>> getClientes() {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de todos os clientes.");

		List<ClienteResponse> clientes = clienteService.getClientes();
		log.info("Clientes encontrados");

		long endTime = System.currentTimeMillis();
		log.info("Busca de todos os clientes concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(clientes);
	}

	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> getClienteById(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Buscando informações do cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}.", usuarioLogado.getId());

		ClienteResponse cliente = clienteService.toResponse(clienteService.getClienteById(id_cliente, usuarioLogado));
		log.info("Informações do cliente obtidas com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Busca de informações do cliente concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(cliente);
	}
	
	// só o admin pode confirmar a exclusão de cadastro de cliente
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id_cliente}")
	public ResponseEntity<Void> deleteCliente(@PathVariable Long id_cliente){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando exclusão do cliente ID: {}.", id_cliente);

		clienteService.deleteCliente(id_cliente);
		log.info("Cliente ID: {} excluído com sucesso.", id_cliente);

		long endTime = System.currentTimeMillis();
		log.info("Exclusão de cliente concluída em {} ms.", endTime - startTime);
		return ResponseEntity.noContent().build();
	}
	
	// admin podem atualizar dados cadastrais, cliente só se for dele
	@PutMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody ClienteAtualizadoDTO clienteAtualizado,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando atualização do cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}.", usuarioLogado.getId());

		ClienteResponse atualizado = clienteService.atualizarCliente(id_cliente, clienteAtualizado, usuarioLogado);
		log.info("Cliente ID: {} atualizado com sucesso.", id_cliente);

		long endTime = System.currentTimeMillis();
		log.info("Atualização de cliente concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(atualizado);
	}
	
	// admin podem atualizar dados cadastrais, cliente só se for dele
	@PatchMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateParcial(
			@PathVariable Long id_cliente,
			@Valid @RequestBody ClienteAtualizadoDTO clienteAtualizado,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando atualização parcial do cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info("Usuário logado: ID: {}.", usuarioLogado.getId());

		ClienteResponse atualizado = clienteService.atualizarCliente(id_cliente, clienteAtualizado, usuarioLogado);
		log.info("Cliente ID: {} atualizado parcialmente com sucesso.", id_cliente);

		long endTime = System.currentTimeMillis();
		log.info("Atualização parcial de cliente concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(atualizado);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/categoria/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateCategoriaCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody AtualizarCategoriaClienteDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando atualização de categoria para cliente ID: {}.", id_cliente);
		
		ClienteResponse atualizado = clienteService.updateCategoriaCliente(id_cliente, dto.getCategoriaCliente());
		log.info("Categoria do cliente atualizada com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Atualização de categoria concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(atualizado);
	}
	
}
