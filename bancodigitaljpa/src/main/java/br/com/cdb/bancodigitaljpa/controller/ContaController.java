package br.com.cdb.bancodigitaljpa.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.dto.AbrirContaDTO;
import br.com.cdb.bancodigitaljpa.dto.DepositoDTO;
import br.com.cdb.bancodigitaljpa.dto.PixDTO;
import br.com.cdb.bancodigitaljpa.dto.SaqueDTO;
import br.com.cdb.bancodigitaljpa.dto.TransferenciaDTO;
import br.com.cdb.bancodigitaljpa.model.Usuario;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.response.AplicarTxManutencaoResponse;
import br.com.cdb.bancodigitaljpa.response.AplicarTxRendimentoResponse;
import br.com.cdb.bancodigitaljpa.response.ContaResponse;
import br.com.cdb.bancodigitaljpa.response.DepositoResponse;
import br.com.cdb.bancodigitaljpa.response.PixResponse;
import br.com.cdb.bancodigitaljpa.response.SaldoResponse;
import br.com.cdb.bancodigitaljpa.response.SaqueResponse;
import br.com.cdb.bancodigitaljpa.response.TransferenciaResponse;
import br.com.cdb.bancodigitaljpa.service.ContaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contas")
public class ContaController {

	@Autowired
	private ContaService contaService;
	
	//ambos podem criar nova conta
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping
	public ResponseEntity<ContaResponse> abrirConta(
			@Valid @RequestBody AbrirContaDTO dto,
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		ContaResponse response = contaService.abrirConta(dto.getId_cliente(), usuarioLogado, dto.getTipoConta(), dto.getMoeda(), dto.getValorDeposito());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);	
	}
	
	// cliente e admin
	@GetMapping("/tipos")
	public ResponseEntity<List<TipoConta>> listarTiposContas() {
		return ResponseEntity.ok(Arrays.asList(TipoConta.values()));
	}
	
	// só admin pode puxar uma lista de todas contas
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<ContaResponse>> getContas() {
		List<ContaResponse> contas = contaService.getContas();
		return ResponseEntity.ok(contas);
	}
	
	// para usuário logado ver informações de suas contas (cliente)
	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("/minhas-contas")
	public ResponseEntity<List<ContaResponse>> buscarContasDoUsuario (
			Authentication authentication){
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		List<ContaResponse> contas = contaService.listarPorUsuario(usuarioLogado);
		return ResponseEntity.ok(contas);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<ContaResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		List<ContaResponse> contas = contaService.listarPorCliente(id_cliente, usuarioLogado);
		return ResponseEntity.ok(contas);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/{id_conta}")
	public ResponseEntity<ContaResponse> getContaById(
			@PathVariable Long id_conta,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		ContaResponse conta = contaService.getContaById(id_conta, usuarioLogado);
		return ResponseEntity.ok(conta);
	}
	
	// só o admin pode confirmar a exclusão de cadastro de contas
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/cliente/{id_cliente}")
	public ResponseEntity<Void> deleteContasByCliente(
			@PathVariable Long id_cliente) {
		contaService.deleteContasByCliente(id_cliente);
		return ResponseEntity.noContent().build();
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	//realizar uma transf entre contas
	@PostMapping("/{id_contaOrigem}/transferencia")
	public ResponseEntity<TransferenciaResponse> transferir(
			@PathVariable Long id_contaOrigem, 
			@Valid @RequestBody TransferenciaDTO dto,
			Authentication authentication) {	
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		TransferenciaResponse response = contaService.transferir(id_contaOrigem, usuarioLogado, dto.getId_contaDestino(), dto.getValor());
		return ResponseEntity.ok(response);
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	//realizar um pg pix
	@PostMapping("/{id_contaOrigem}/pix")
	public ResponseEntity<PixResponse> pix(
			@PathVariable Long id_contaOrigem, 
			@Valid @RequestBody PixDTO dto,
			Authentication authentication)	{			
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		PixResponse response = contaService.pix(id_contaOrigem, usuarioLogado, dto.getId_contaDestino(), dto.getValor());
		return ResponseEntity.ok(response);
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	//consultar saldo da conta
	@GetMapping("/{id_conta}/saldo")
	public ResponseEntity<SaldoResponse> getSaldo(
			@PathVariable Long id_conta,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		SaldoResponse reponse = contaService.getSaldo(id_conta, usuarioLogado);
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
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	//sacar da conta
	@PostMapping("/{id_conta}/saque")
	public ResponseEntity<SaqueResponse> sacar(
			@PathVariable Long id_conta, 
			@Valid @RequestBody SaqueDTO dto,
			Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		SaqueResponse response = contaService.sacar(id_conta, usuarioLogado, dto.getValor());
		return ResponseEntity.ok(response);
	}
	
	//debitar tx mensal manut CC
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_conta}/manutencao")
	public ResponseEntity<AplicarTxManutencaoResponse> aplicarTxManutencao(
			@PathVariable Long id_conta){
		AplicarTxManutencaoResponse response = contaService.debitarTarifaManutencao(id_conta);
		return ResponseEntity.ok(response);
		
	}
	
	//creditar mensal rend CP
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_conta}/rendimentos")
	public ResponseEntity<AplicarTxRendimentoResponse> aplicarTxRendimento(
			@PathVariable Long id_conta){
		AplicarTxRendimentoResponse response = contaService.creditarRendimento(id_conta);
		return ResponseEntity.ok(response);	
	}
	
	

}
