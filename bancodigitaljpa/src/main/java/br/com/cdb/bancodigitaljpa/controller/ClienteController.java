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

	// admin e cliente podem cadastrar, porém aqui só cliente 
	@PostMapping
	public ResponseEntity<ClienteResponse> cadastrarCliente(
			@Valid @RequestBody ClienteDTO dto,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		ClienteResponse response = clienteService.cadastrarCliente(dto, usuarioLogado);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// só faz sentido do cliente neste caso
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

	// o cliente e admin poderiam ver, mas o ideal é que o cliente pegue o id direto do usuario logado apenas ne? entao esse get fica mais pro admin
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> getClienteById(@PathVariable Long id_cliente) {
		ClienteResponse cliente = clienteService.getClienteById(id_cliente);
		return ResponseEntity.ok(cliente);
	}
	
	// aqui só o admin deveria poder confirmar essa exclusão, mas o cliente deveria poder solicitar a exclusão da sua conta
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id_cliente}")
	public ResponseEntity<Void> deleteCliente(@PathVariable Long id_cliente){		
		clienteService.deleteCliente(id_cliente);
		return ResponseEntity.noContent().build();
	}
	
	// aqui o cliente e admin podem atualizar dados cadastrais
	@PutMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody ClienteDTO clienteAtualizado,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		ClienteResponse atualizado = clienteService.updateCliente(id_cliente, clienteAtualizado, usuarioLogado);
			return ResponseEntity.ok(atualizado);
	}
	
	// aqui o cliente e admin podem atualizar dados cadastrais
	@PatchMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateParcial(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody Map<String, Object> camposAtualizados){
		
		ClienteResponse atualizado = clienteService.updateParcial(id_cliente, camposAtualizados);
			return ResponseEntity.ok(atualizado);
	}
	
	// aqui só admin pode alterar
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_cliente}/categoria")
	public ResponseEntity<ClienteResponse> updateCategoriaCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody AtualizarCategoriaClienteDTO dto){
		
		CategoriaCliente novaCategoria = dto.getCategoriaCliente();
		ClienteResponse atualizado = clienteService.updateCategoriaCliente(id_cliente, novaCategoria);
			return ResponseEntity.ok(atualizado);
	}
	
}
