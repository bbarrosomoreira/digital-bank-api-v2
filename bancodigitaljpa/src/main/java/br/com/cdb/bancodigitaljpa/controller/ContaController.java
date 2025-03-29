package br.com.cdb.bancodigitaljpa.controller;

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

import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.service.ContaService;

@RestController
@RequestMapping("/contas")
public class ContaController {

	@Autowired
	private ContaService contaService;
	
	//criar nova conta
	@PostMapping("/add")
	public ResponseEntity<String> addConta(@RequestParam String tipoConta,
			@RequestParam Long id_cliente){
		
		try {
			TipoConta tipo = TipoConta.valueOf(tipoConta.toUpperCase());
			ContaBase contaAdicionada = switch (tipo) {
			case CORRENTE -> contaService.addContaCorrente(id_cliente);
			case POUPANCA -> contaService.addContaPoupanca(id_cliente);
			};
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(contaAdicionada.getTipo() + " adicionada com sucesso! ID: " + contaAdicionada.getId());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Tipo de conta inválido. Use CORRENTE ou POUPANCA.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
		}
		
	}
	
	@GetMapping("/listAll")
	public ResponseEntity<List<ContaBase>> getContas() {
		List<ContaBase> contas = contaService.getContas();
		return new ResponseEntity<List<ContaBase>>(contas, HttpStatus.OK);
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
