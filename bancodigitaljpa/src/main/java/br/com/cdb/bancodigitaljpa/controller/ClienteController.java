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

import br.com.cdb.bancodigitaljpa.dto.AtualizarCategoriaClienteDTO;
import br.com.cdb.bancodigitaljpa.dto.CriarClienteDTO;
import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.response.ClienteResponse;
import br.com.cdb.bancodigitaljpa.response.CpfValidationResponse;
import br.com.cdb.bancodigitaljpa.service.ClienteService;
import br.com.cdb.bancodigitaljpa.service.ReceitaCpfService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ReceitaCpfService cpfValidationService;
	
	@GetMapping("/consultar-cpf/{cpf}")
	public ResponseEntity<CpfValidationResponse> consultarCpf(@PathVariable String cpf) {
		CpfValidationResponse response = cpfValidationService.consultarCpf(cpf);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<ClienteResponse> addCliente(@Valid @RequestBody CriarClienteDTO dto) {
		ClienteResponse response = clienteService.addCliente(dto.transformaParaObjeto());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<ClienteResponse>> getClientes() {
		List<ClienteResponse> clientes = clienteService.getClientes();
		return ResponseEntity.ok(clientes);
	}

	@GetMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> getClienteById(@PathVariable Long id_cliente) {
		ClienteResponse cliente = clienteService.getClienteById(id_cliente);
		return ResponseEntity.ok(cliente);
	}
	
	@DeleteMapping("/{id_cliente}")
	public ResponseEntity<Void> removeCliente(@PathVariable Long id_cliente){		
		clienteService.deleteCliente(id_cliente);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody Cliente clienteAtualizado){
		
		ClienteResponse atualizado = clienteService.updateCliente(id_cliente, clienteAtualizado);
			return ResponseEntity.ok(atualizado);
	}
	
	@PatchMapping("/{id_cliente}")
	public ResponseEntity<ClienteResponse> updateParcial(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody Map<String, Object> camposAtualizados){
		
		ClienteResponse atualizado = clienteService.updateParcial(id_cliente, camposAtualizados);
			return ResponseEntity.ok(atualizado);
	}
	
	@PutMapping("/{id_cliente}/categoria")
	public ResponseEntity<ClienteResponse> updateCategoriaCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody AtualizarCategoriaClienteDTO dto){
		
		CategoriaCliente novaCategoria = dto.getCategoriaCliente();
		ClienteResponse atualizado = clienteService.updateCategoriaCliente(id_cliente, novaCategoria);
			return ResponseEntity.ok(atualizado);
	}
	
}
