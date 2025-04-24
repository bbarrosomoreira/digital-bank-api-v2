package br.com.cdb.bancodigitaljpa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.cdb.bancodigitaljpa.dto.AtualizarCategoriaClienteDTO;
import br.com.cdb.bancodigitaljpa.dto.ClienteDTO;
import br.com.cdb.bancodigitaljpa.entity.Usuario;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.response.ClienteResponse;
import br.com.cdb.bancodigitaljpa.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping
	public ResponseEntity<ClienteResponse> cadastrarCliente(
			@Valid @RequestBody ClienteDTO dto,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		ClienteResponse response = clienteService.cadastrarCliente(dto, usuarioLogado);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// para usuário logado ver suas informações (cliente)
	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("/me")
	public ResponseEntity<ClienteResponse> buscarClienteDoUsuario(
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
	    ClienteResponse cliente = clienteService.buscarClienteDoUsuario(usuarioLogado);
	    return ResponseEntity.ok(cliente);
	}

	// só admin pode puxar uma lista de todos clientes
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<ClienteResponse>> getClientes() {
		List<ClienteResponse> clientes = clienteService.getClientes();
		return ResponseEntity.ok(clientes);
	}

	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> getClienteById(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		ClienteResponse cliente = clienteService.toResponse(clienteService.getClienteById(id_cliente, usuarioLogado));
		return ResponseEntity.ok(cliente);
	}
	
	// só o admin pode confirmar a exclusão de cadastro de cliente
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id_cliente}")
	public ResponseEntity<Void> deleteCliente(@PathVariable Long id_cliente){		
		clienteService.deleteCliente(id_cliente);
		return ResponseEntity.noContent().build();
	}
	
	// admin podem atualizar dados cadastrais, cliente só se for dele
	@PutMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody ClienteDTO clienteAtualizado,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		ClienteResponse atualizado = clienteService.updateCliente(id_cliente, clienteAtualizado, usuarioLogado);
			return ResponseEntity.ok(atualizado);
	}
	
	// admin podem atualizar dados cadastrais, cliente só se for dele
	@PatchMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateParcial(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody Map<String, Object> camposAtualizados,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		ClienteResponse atualizado = clienteService.updateParcial(id_cliente, camposAtualizados, usuarioLogado);
			return ResponseEntity.ok(atualizado);
	}
	
	// só admin pode alterar a categoria do cliente
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/categoria/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateCategoriaCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody AtualizarCategoriaClienteDTO dto) {
		
		CategoriaCliente novaCategoria = dto.getCategoriaCliente();
		ClienteResponse atualizado = clienteService.updateCategoriaCliente(id_cliente, novaCategoria);
			return ResponseEntity.ok(atualizado);
	}
	
}
