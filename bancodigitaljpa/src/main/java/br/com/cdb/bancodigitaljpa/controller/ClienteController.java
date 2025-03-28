package br.com.cdb.bancodigitaljpa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@PostMapping("/add")
	public ResponseEntity<String> addCliente(@Valid @RequestBody Cliente cliente) {

		Cliente clienteAdicionado = clienteService.addCliente(cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento());

		if (clienteAdicionado != null) {
			return new ResponseEntity<>("Cliente " + cliente.getNome() + " adicionado com sucesso!",
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Informação do cliente inválida!", HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/listAll")
	public ResponseEntity<List<Cliente>> getClientes() {
		List<Cliente> clientes = clienteService.getClientes();
		return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
		Cliente cliente = clienteService.getClienteById(id);
		return ResponseEntity.ok(cliente);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> removeCliente(@PathVariable Long id){		
		if (clienteService.deleteCliente(id)) {
			return new ResponseEntity<>("Cliente ID " + id + " deletado com sucesso",
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Falha ao deletar cliente com ID " + id, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Cliente> updateCliente(
			@PathVariable Long id, 
			@Valid @RequestBody Cliente clienteAtualizado){
		
		Cliente atualizado = clienteService.updateCliente(id, clienteAtualizado);
			return ResponseEntity.ok(atualizado);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Cliente> updateParcial(
			@PathVariable Long id, 
			@RequestBody Map<String, Object> camposAtualizados){
		
		Cliente atualizado = clienteService.updateParcial(id, camposAtualizados);
			return ResponseEntity.ok(atualizado);
	}
	
}
