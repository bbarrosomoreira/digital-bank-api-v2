package br.com.cdb.bancodigital.controller;

import java.util.Arrays;
import java.util.List;

import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import br.com.cdb.bancodigital.dto.AbrirContaDTO;
import br.com.cdb.bancodigital.dto.DepositoDTO;
import br.com.cdb.bancodigital.dto.PixDTO;
import br.com.cdb.bancodigital.dto.SaqueDTO;
import br.com.cdb.bancodigital.dto.TransferenciaDTO;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import br.com.cdb.bancodigital.dto.response.AplicarTxManutencaoResponse;
import br.com.cdb.bancodigital.dto.response.AplicarTxRendimentoResponse;
import br.com.cdb.bancodigital.dto.response.ContaResponse;
import br.com.cdb.bancodigital.dto.response.DepositoResponse;
import br.com.cdb.bancodigital.dto.response.PixResponse;
import br.com.cdb.bancodigital.dto.response.SaldoResponse;
import br.com.cdb.bancodigital.dto.response.SaqueResponse;
import br.com.cdb.bancodigital.dto.response.TransferenciaResponse;
import br.com.cdb.bancodigital.service.ContaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contas")
@AllArgsConstructor
@Slf4j
public class ContaController {

	private final ContaService contaService;
	
	//ambos podem criar conta
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize("hasRole('CLIENTE')")
	@PostMapping
	public ResponseEntity<ContaResponse> abrirConta(
			@Valid @RequestBody AbrirContaDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando abertura de conta.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ContaResponse response = contaService.abrirConta(dto.getId_cliente(), usuarioLogado, dto.getTipoConta(), dto.getMoeda(), dto.getValorDeposito());
		log.info("Conta criada com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Abertura de conta concluída em {} ms.", endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);	
	}
	
	// cliente e admin
	@GetMapping("/tipos")
	public ResponseEntity<List<TipoConta>> listarTiposContas() {
		log.info("Listando tipos de contas disponíveis.");
		return ResponseEntity.ok(Arrays.asList(TipoConta.values()));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<ContaResponse>> getContas() {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de todas as contas.");

		List<ContaResponse> contas = contaService.getContas();
		log.info("Contas encontradas");

		long endTime = System.currentTimeMillis();
		log.info("Busca de contas concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(contas);
	}
	
	// para usuário logado ver informações de suas contas (cliente)
	@PreAuthorize("hasRole('CLIENTE')")
	@GetMapping("/minhas-contas")
	public ResponseEntity<List<ContaResponse>> buscarContasDoUsuario (
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info("Buscando contas do usuário logado.");

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<ContaResponse> contas = contaService.listarPorUsuario(usuarioLogado);
		log.info("Contas encontradas");

		long endTime = System.currentTimeMillis();
		log.info("Busca de contas do usuário logado concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(contas);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/cliente/{id_cliente}")
	public ResponseEntity<List<ContaResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Buscando contas para cliente ID: {}.", id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<ContaResponse> contas = contaService.listarPorCliente(id_cliente, usuarioLogado);
		log.info("Contas encontradas");

		long endTime = System.currentTimeMillis();
		log.info("Busca de contas por cliente concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(contas);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping("/{id_conta}")
	public ResponseEntity<ContaResponse> getContaById(
			@PathVariable Long id_conta,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Buscando informações da conta ID: {}.", id_conta);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ContaResponse conta = contaService.getContaById(id_conta, usuarioLogado);
		log.info("Informações da conta obtidas com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Busca de informações da conta concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(conta);
	}
	
	// só o admin pode confirmar a exclusão de cadastro de contas
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/cliente/{id_cliente}")
	public ResponseEntity<Void> deleteContasByCliente(
			@PathVariable Long id_cliente) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando exclusão de contas para cliente ID: {}.", id_cliente);

		contaService.deleteContasByCliente(id_cliente);
		log.info("Contas excluídas com sucesso para cliente ID: {}.", id_cliente);

		long endTime = System.currentTimeMillis();
		log.info("Exclusão de contas concluída em {} ms.", endTime - startTime);
		return ResponseEntity.noContent().build();
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	//realizar uma transf entre contas
	@PostMapping("/{id_contaOrigem}/transferencia")
	public ResponseEntity<TransferenciaResponse> transferir(
			@PathVariable Long id_contaOrigem, 
			@Valid @RequestBody TransferenciaDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando transferência da conta ID: {}.", id_contaOrigem);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		TransferenciaResponse response = contaService.transferir(id_contaOrigem, usuarioLogado, dto.getId_contaDestino(), dto.getValor());
		log.info("Transferência realizada com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Transferência concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	@PostMapping("/{id_contaOrigem}/pix")
	public ResponseEntity<PixResponse> pix(
			@PathVariable Long id_contaOrigem, 
			@Valid @RequestBody PixDTO dto,
			Authentication authentication)	{
		long startTime = System.currentTimeMillis();
		log.info("Iniciando pagamento PIX da conta ID: {}.", id_contaOrigem);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		PixResponse response = contaService.pix(id_contaOrigem, usuarioLogado, dto.getId_contaDestino(), dto.getValor());
		log.info("Pagamento PIX realizado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Pagamento PIX concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	@GetMapping("/{id_conta}/saldo")
	public ResponseEntity<SaldoResponse> getSaldo(
			@PathVariable Long id_conta,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Consultando saldo da conta ID: {}.", id_conta);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		SaldoResponse response = contaService.getSaldo(id_conta, usuarioLogado);
		log.info("Saldo consultado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Consulta de saldo concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("{id_conta}/deposito")
	public ResponseEntity<DepositoResponse> depositar(
			@PathVariable Long id_conta, 
			@Valid @RequestBody DepositoDTO dto)
	{
		long startTime = System.currentTimeMillis();
		log.info("Iniciando depósito na conta ID: {}.", id_conta);

		DepositoResponse response = contaService.depositar(id_conta, dto.getValor());
		log.info("Depósito realizado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Depósito concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	@PostMapping("/{id_conta}/saque")
	public ResponseEntity<SaqueResponse> sacar(
			@PathVariable Long id_conta, 
			@Valid @RequestBody SaqueDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando saque da conta ID: {}.", id_conta);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		SaqueResponse response = contaService.sacar(id_conta, usuarioLogado, dto.getValor());
		log.info("Saque realizado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Saque concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	//debitar tarifa de manutenção MENSAL de Conta Corrente e Conta Internacional
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_conta}/manutencao")
	public ResponseEntity<AplicarTxManutencaoResponse> aplicarTxManutencao(
			@PathVariable Long id_conta){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando aplicação de taxa de manutenção para conta ID: {}.", id_conta);

		AplicarTxManutencaoResponse response = contaService.debitarTarifaManutencao(id_conta);
		log.info("Taxa de manutenção aplicada com sucesso para conta");

		long endTime = System.currentTimeMillis();
		log.info("Aplicação de taxa de manutenção concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
		
	}
	
	//creditar rendimento MENSAL de Conta Poupança
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id_conta}/rendimentos")
	public ResponseEntity<AplicarTxRendimentoResponse> aplicarTxRendimento(
			@PathVariable Long id_conta){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando aplicação de rendimentos para conta ID: {}.", id_conta);

		AplicarTxRendimentoResponse response = contaService.creditarRendimento(id_conta);
		log.info("Rendimentos aplicados com sucesso para conta.");

		long endTime = System.currentTimeMillis();
		log.info("Aplicação de rendimentos concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);	
	}
	
	

}
