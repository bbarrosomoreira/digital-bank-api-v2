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
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.dto.AbrirContaDTO;
import br.com.cdb.bancodigitaljpa.dto.AplicarTxManutencaoResponse;
import br.com.cdb.bancodigitaljpa.dto.AplicarTxRendimentoResponse;
import br.com.cdb.bancodigitaljpa.dto.ContaResponse;
import br.com.cdb.bancodigitaljpa.dto.DepositoDTO;
import br.com.cdb.bancodigitaljpa.dto.DepositoResponse;
import br.com.cdb.bancodigitaljpa.dto.PixDTO;
import br.com.cdb.bancodigitaljpa.dto.PixResponse;
import br.com.cdb.bancodigitaljpa.dto.SaldoResponse;
import br.com.cdb.bancodigitaljpa.dto.SaqueDTO;
import br.com.cdb.bancodigitaljpa.dto.SaqueResponse;
import br.com.cdb.bancodigitaljpa.dto.TransferenciaDTO;
import br.com.cdb.bancodigitaljpa.dto.TransferenciaResponse;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.service.ContaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contas")
public class ContaController {

	@Autowired
	private ContaService contaService;
	
	//criar nova conta
	@PostMapping
	public ResponseEntity<ContaResponse> abrirConta(@RequestBody AbrirContaDTO dto){
		ContaBase contaNova = contaService.abrirConta(dto.getId_cliente(), dto.getTipoConta());
		ContaResponse response = contaService.toResponse(contaNova);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	@GetMapping("/tipos")
	public ResponseEntity<List<TipoConta>> listarTiposContas() {
		return ResponseEntity.ok(Arrays.asList(TipoConta.values()));
	}
	
	@GetMapping
	public ResponseEntity<List<ContaResponse>> getContas() {
		List<ContaResponse> contas = contaService.getContas();
		return ResponseEntity.ok(contas);
	}
	
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<ContaResponse>> listarPorCliente(
			@PathVariable Long id_cliente) {
		List<ContaResponse> contas = contaService.listarPorCliente(id_cliente);
		return ResponseEntity.ok(contas);
	}
	
	@GetMapping("/{id_conta}")
	public ResponseEntity<ContaResponse> getContaById(
			@PathVariable Long id_conta) {
		ContaResponse conta = contaService.getContaById(id_conta);
		return ResponseEntity.ok(conta);
	}
	
	//realizar uma transf entre contas
	@PostMapping("/{id_contaOrigem}/transferencia")
	public ResponseEntity<TransferenciaResponse> transferir(
			@PathVariable Long id_contaOrigem, 
			@Valid @RequestBody TransferenciaDTO dto)
	{	
		TransferenciaResponse response = contaService.transferir(id_contaOrigem, dto.getId_contaDestino(), dto.getValor());
		return ResponseEntity.ok(response);
	}
	
	//realizar um pg pix
	@PostMapping("/{id_contaOrigem}/pix")
	public ResponseEntity<PixResponse> pix(
			@PathVariable Long id_contaOrigem, 
			@Valid @RequestBody PixDTO dto)
	{	
		PixResponse response = contaService.pix(id_contaOrigem, dto.getId_contaDestino(), dto.getValor());
		return ResponseEntity.ok(response);
	}
	
	//consultar saldo da conta
	@GetMapping("/{id_conta}/saldo")
	public ResponseEntity<SaldoResponse> getSaldo(
			@PathVariable Long id_conta) {
		SaldoResponse reponse = contaService.getSaldo(id_conta);
		return ResponseEntity.ok(reponse);
	}
	
	//depositar em conta
	@PostMapping("{id_conta}/deposito")
	public ResponseEntity<DepositoResponse> depositar(
			@PathVariable Long id_conta, 
			@Valid @RequestBody DepositoDTO dto)
	{	
		DepositoResponse response = contaService.depositar(id_conta, dto.getValor());
		return ResponseEntity.ok(response);
	}
	
	//sacar da conta
	@PostMapping("/{id_conta}/saque")
	public ResponseEntity<SaqueResponse> sacar(
			@PathVariable Long id_conta, 
			@Valid @RequestBody SaqueDTO dto)
	{	
		SaqueResponse response = contaService.sacar(id_conta, dto.getValor());
		return ResponseEntity.ok(response);
	}
	
	//debitar tx mensal manut CC
	@PutMapping("/{id_conta}/manutencao")
	public ResponseEntity<AplicarTxManutencaoResponse> aplicarTxManutencao(
			@PathVariable Long id_conta){
		AplicarTxManutencaoResponse response = contaService.debitarTarifaManutencao(id_conta);
		return ResponseEntity.ok(response);
		
	}
	
	//creditar mensal rend CP
	@PutMapping("/{id_conta}/rendimentos")
	public ResponseEntity<AplicarTxRendimentoResponse> aplicarTxRendimento(
			@PathVariable Long id_conta){
		AplicarTxRendimentoResponse response = contaService.creditarRendimento(id_conta);
		return ResponseEntity.ok(response);
		
	}
	
	

}
