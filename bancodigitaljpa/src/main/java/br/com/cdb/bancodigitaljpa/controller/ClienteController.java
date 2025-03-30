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

import br.com.cdb.bancodigitaljpa.dto.ClienteDTO;
import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
//	@Autowired
//	private CorreiosService correiosService;

	@PostMapping("/add")
	public ResponseEntity<Cliente> addCliente(@Valid @RequestBody ClienteDTO dto) {
		
//		EnderecoCliente enderecoCliente = correiosService.consultarEConverterCep(clienteDto.getEndereco().getCep());
//		String rua, int numero, String complemento, String cidade, String estado, String cep
//		EnderecoCliente enderecoCliente = new EnderecoCliente(
//				clienteDto.getEndereco().getRua(), 
//				clienteDto.getEndereco().getNumero(),
//				clienteDto.getEndereco().getComplemento(),
//				clienteDto.getEndereco().getCidade(),
//				clienteDto.getEndereco().getEstado(),
//				clienteDto.getEndereco().getCep());
		
//		Cliente cliente = clienteDto.transformaParaObjeto();
		
		Cliente clienteAdicionado = clienteService.addCliente(dto.transformaParaObjeto());
//		ClienteResponse response = clienteService.toResponse(clienteAdicionado);
		return new ResponseEntity<>(clienteAdicionado, HttpStatus.CREATED);

//		if (clienteAdicionado != null) {
//			return new ResponseEntity<>("Cliente " + cliente.getNome() + " adicionado com sucesso!",
//					HttpStatus.CREATED);
//		} else {
//			return new ResponseEntity<>("Informação do cliente inválida!", HttpStatus.NOT_ACCEPTABLE);
//		}
	}

	@GetMapping("/listAll")
	public ResponseEntity<List<Cliente>> getClientes() {
		List<Cliente> clientes = clienteService.getClientes();
		return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
	}

	@GetMapping("/{id_cliente}")
	public ResponseEntity<Cliente> getClienteById(@PathVariable Long id_cliente) {
		Cliente cliente = clienteService.getClienteById(id_cliente);
		return ResponseEntity.ok(cliente);
	}
	
	@DeleteMapping("/{id_cliente}")
	public ResponseEntity<String> removeCliente(@PathVariable Long id_cliente){		
		if (clienteService.deleteCliente(id_cliente)) {
			return new ResponseEntity<>("Cliente ID " + id_cliente + " deletado com sucesso",
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Falha ao deletar cliente com ID " + id_cliente, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@PutMapping("/{id_cliente}")
	public ResponseEntity<Cliente> updateCliente(
			@PathVariable Long id_cliente, 
			@Valid @RequestBody Cliente clienteAtualizado){
		
		Cliente atualizado = clienteService.updateCliente(id_cliente, clienteAtualizado);
			return ResponseEntity.ok(atualizado);
	}
	
	@PatchMapping("/{id_cliente}")
	public ResponseEntity<Cliente> updateParcial(
			@PathVariable Long id_cliente, 
			@RequestBody Map<String, Object> camposAtualizados){
		
		Cliente atualizado = clienteService.updateParcial(id_cliente, camposAtualizados);
			return ResponseEntity.ok(atualizado);
	}
	
}
