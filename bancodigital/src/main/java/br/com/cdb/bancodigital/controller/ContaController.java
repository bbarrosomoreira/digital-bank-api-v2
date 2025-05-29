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
@RequestMapping(ConstantUtils.CONTA)
@AllArgsConstructor
@Slf4j
public class ContaController {

	private final ContaService contaService;
	
	// só cliente pode cadastrar por este endpoint, pois ele vincula o cadastro ao login
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@PostMapping
	public ResponseEntity<ContaResponse> abrirConta(
			@Valid @RequestBody AbrirContaDTO dto,
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_ABERTURA_CONTA);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ContaResponse response = contaService.abrirConta(dto.getId_cliente(), usuarioLogado, dto.getTipoConta(), dto.getMoeda(), dto.getValorDeposito());
		log.info(ConstantUtils.SUCESSO_ABERTURA_CONTA, response.getId());

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);	
	}
	
	// cliente e admin
	@GetMapping(ConstantUtils.TIPOS)
	public ResponseEntity<List<TipoConta>> listarTiposContas() {
		log.info(ConstantUtils.INICIO_LISTAGEM_TIPO_CONTA);
		return ResponseEntity.ok(Arrays.asList(TipoConta.values()));
	}
	
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@GetMapping
	public ResponseEntity<List<ContaResponse>> getContas() {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CONTA);

		List<ContaResponse> contas = contaService.getContas();
		log.info(ConstantUtils.SUCESSO_BUSCA_CONTA);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(contas);
	}
	
	// para usuário logado ver informações de suas contas (cliente)
	@PreAuthorize(ConstantUtils.ROLE_CLIENTE)
	@GetMapping(ConstantUtils.GET_USUARIO)
	public ResponseEntity<List<ContaResponse>> buscarContasDoUsuario (
			Authentication authentication){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CONTA);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<ContaResponse> contas = contaService.listarPorUsuario(usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_CONTA);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(contas);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.CLIENTE + ConstantUtils.CLIENTE_ID)
	public ResponseEntity<List<ContaResponse>> listarPorCliente(
			@PathVariable Long id_cliente,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CONTA + ConstantUtils.ID_CLIENTE, id_cliente);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		List<ContaResponse> contas = contaService.listarPorCliente(id_cliente, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_CONTA + ConstantUtils.ID_CLIENTE, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(contas);
	}
	
	// admin tem acesso ao id, cliente só pode ver se for dele
	@GetMapping(ConstantUtils.CONTA_ID)
	public ResponseEntity<ContaResponse> getContaById(
			@PathVariable Long id_conta,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_CONTA + ConstantUtils.ID_CONTA, id_conta);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		ContaResponse conta = contaService.getContaById(id_conta, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_BUSCA_CONTA + ConstantUtils.ID_CONTA, id_conta);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(conta);
	}
	
	// só o admin pode confirmar a exclusão de cadastro de contas
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@DeleteMapping(ConstantUtils.CLIENTE + ConstantUtils.CLIENTE_ID)
	public ResponseEntity<Void> deleteContasByCliente(
			@PathVariable Long id_cliente) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_DELETE_CONTA, id_cliente);

		contaService.deleteContasByCliente(id_cliente);
		log.info(ConstantUtils.SUCESSO_DELETE_CONTA, id_cliente);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.noContent().build();
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	//realizar uma transf entre contas
	@PostMapping(ConstantUtils.CONTA_ORIGEM_ID + ConstantUtils.TRANSFERENCIA_ENDPOINT)
	public ResponseEntity<TransferenciaResponse> transferir(
			@PathVariable Long id_contaOrigem, 
			@Valid @RequestBody TransferenciaDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.TRANSFERENCIA, id_contaOrigem);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		TransferenciaResponse response = contaService.transferir(id_contaOrigem, usuarioLogado, dto.getId_contaDestino(), dto.getValor());
		log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, ConstantUtils.TRANSFERENCIA);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	@PostMapping(ConstantUtils.CONTA_ORIGEM_ID + ConstantUtils.PIX_ENDPOINT)
	public ResponseEntity<PixResponse> pix(
			@PathVariable Long id_contaOrigem, 
			@Valid @RequestBody PixDTO dto,
			Authentication authentication)	{
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.PIX, id_contaOrigem);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		PixResponse response = contaService.pix(id_contaOrigem, usuarioLogado, dto.getId_contaDestino(), dto.getValor());
		log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, ConstantUtils.PIX);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	@GetMapping(ConstantUtils.CONTA_ID + ConstantUtils.SALDO_ENDPOINT)
	public ResponseEntity<SaldoResponse> getSaldo(
			@PathVariable Long id_conta,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_LEITURA_SALDO, id_conta);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		SaldoResponse response = contaService.getSaldo(id_conta, usuarioLogado);
		log.info(ConstantUtils.SUCESSO_LEITURA_SALDO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(ConstantUtils.CONTA_ID + ConstantUtils.DEPOSITO_ENDPOINT)
	public ResponseEntity<DepositoResponse> depositar(
			@PathVariable Long id_conta, 
			@Valid @RequestBody DepositoDTO dto)
	{
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.DEPOSITO, id_conta);

		DepositoResponse response = contaService.depositar(id_conta, dto.getValor());
		log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, ConstantUtils.DEPOSITO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// admin tem acesso ao id, cliente só pode se origem for dele
	@PostMapping(ConstantUtils.CONTA_ID + ConstantUtils.SAQUE_ENDPOINT)
	public ResponseEntity<SaqueResponse> sacar(
			@PathVariable Long id_conta, 
			@Valid @RequestBody SaqueDTO dto,
			Authentication authentication) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.SAQUE, id_conta);

		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		log.info(ConstantUtils.USUARIO_LOGADO, usuarioLogado.getId());

		SaqueResponse response = contaService.sacar(id_conta, usuarioLogado, dto.getValor());
		log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, ConstantUtils.SAQUE);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	//debitar tarifa de manutenção MENSAL de Conta Corrente e Conta Internacional
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PutMapping(ConstantUtils.CONTA_ID + ConstantUtils.MANUTENCAO_ENDPOINT)
	public ResponseEntity<AplicarTxManutencaoResponse> aplicarTxManutencao(
			@PathVariable Long id_conta){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.APLICACAO_TARIFA_MANUTENCAO, id_conta);

		AplicarTxManutencaoResponse response = contaService.debitarTarifaManutencao(id_conta);
		log.info(ConstantUtils.SUCESSO_TRANSACAO_CONTA, ConstantUtils.APLICACAO_TARIFA_MANUTENCAO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
		
	}
	
	//creditar rendimento MENSAL de Conta Poupança
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@PutMapping(ConstantUtils.CONTA_ID + ConstantUtils.RENDIMENTOS_ENDPOINT)
	public ResponseEntity<AplicarTxRendimentoResponse> aplicarTxRendimento(
			@PathVariable Long id_conta){
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.APLICACAO_RENDIMENTO, id_conta);

		AplicarTxRendimentoResponse response = contaService.creditarRendimento(id_conta);
		log.info(ConstantUtils.INICIO_TRANSACAO_CONTA, ConstantUtils.APLICACAO_RENDIMENTO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);	
	}
	
	

}
