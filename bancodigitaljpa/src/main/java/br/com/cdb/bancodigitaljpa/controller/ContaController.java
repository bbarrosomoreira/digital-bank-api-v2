package br.com.cdb.bancodigitaljpa.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.dto.ContaDTO;
import br.com.cdb.bancodigitaljpa.dto.ContaResponse;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.service.ContaService;

@RestController
@RequestMapping("/contas")
public class ContaController {

	@Autowired
	private ContaService contaService;
	
	//criar nova conta
	@PostMapping("/add")
	public ResponseEntity<ContaResponse> abrirConta(@RequestBody ContaDTO dto){
		ContaBase contaNova = contaService.abrirConta(dto.getId_cliente(), dto.getTipoConta());
		ContaResponse response = contaService.toResponse(contaNova);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
//		try {
//			TipoConta tipo = TipoConta.valueOf(tipoConta.toUpperCase());
//			ContaBase contaAdicionada = switch (tipo) {
//			case CORRENTE -> contaService.addContaCorrente(id_cliente);
//			case POUPANCA -> contaService.addContaPoupanca(id_cliente);
//			};
//			
//			return ResponseEntity.status(HttpStatus.CREATED)
//					.body(contaAdicionada.getTipo() + " adicionada com sucesso! ID: " + contaAdicionada.getId());
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.badRequest().body("Tipo de conta inválido. Use CORRENTE ou POUPANCA.");
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
//		}
		
	}
	
	@GetMapping("/tipos")
	public ResponseEntity<List<TipoConta>> listarTiposContas() {
		return ResponseEntity.ok(Arrays.asList(TipoConta.values()));
	}
	
	@GetMapping("/listAll")
	public ResponseEntity<List<ContaResponse>> getContas() {
		List<ContaResponse> contas = contaService.getContas();
		return ResponseEntity.ok(contas);
	}
	
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<ContaResponse>> listarPorCliente(@PathVariable Long id_cliente) {
		List<ContaResponse> contas = contaService.listarPorCliente(id_cliente);
		return ResponseEntity.ok(contas);
	}
	
	@GetMapping("/{id_conta}")
	public ResponseEntity<ContaResponse> getContaById(@PathVariable Long id_conta) {
		ContaResponse conta = contaService.getContaById(id_conta);
		return ResponseEntity.ok(conta);
	}
	

//	@GetMapping("/{id}")
//	//obter detalhes de uma conta
//	public ResponseEntity<ContaBase> getContaById(@PathVariable Long id, @RequestParam String tipoConta ) {
//		ContaBase conta;
//		if (tipoConta.equals(TipoConta.CORRENTE)) {
//			((ContaCorrente) conta) = contaService.getContaCorrenteById(id);
//		}
//
//		if (tipoConta.equals(TipoConta.POUPANCA)) {
//			((ContaPoupanca) conta) = contaService.getContaPoupancaById(id);
//		}
//
//		
//		return ResponseEntity.ok(conta);
//	}
	
	//@PostMapping("/{id}/transferencia")
	//realizar uma transf entre contas
	
	//@PostMapping("/{id}/pix")
	//realizar um pg pix
	
	//@GetMapping("/{id}/saldo")
	//consultar saldo da conta
	
	//@PostMapping("{id}/deposito")
	//depositar em conta
	
	//@PostMapping("/{id}/saque")
	//sacar da conta
	
	//@PutMapping("/{id}/manutencao")
	//debitar tx mensal manut CC
	
	//@PutMapping("/{id}/rendimentos")
	//creditar mensal rend CP
	
	

}
