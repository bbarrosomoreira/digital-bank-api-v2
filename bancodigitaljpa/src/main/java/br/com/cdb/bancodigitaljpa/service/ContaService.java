package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljpa.dto.ContaResponse;
import br.com.cdb.bancodigitaljpa.dto.SaldoResponse;
import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.exceptions.ContaNaoEncontradaException;
import br.com.cdb.bancodigitaljpa.exceptions.SaldoInsuficienteException;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import jakarta.transaction.Transactional;

@Service
public class ContaService {
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	//addConta de forma genérica
	@Transactional
	public ContaBase abrirConta(Long id_cliente, TipoConta tipo) {
		
		if(tipo == null) {
			throw new IllegalArgumentException("Tipo de conta inválido.");
		}
		
		Cliente cliente = clienteService.getClienteById(id_cliente);
		
		ContaBase contaNova = switch(tipo) {
		case CORRENTE -> new ContaCorrente(cliente);
		case POUPANCA -> new ContaPoupanca(cliente);
		};
		return contaRepository.save(contaNova);
		
	}
	
	public ContaCorrente addContaCorrente(Long id_cliente) {
		//VALIDAR SE CLIENTE EXISTE
		Cliente cliente = clienteService.getClienteById(id_cliente);
		ContaCorrente conta = new ContaCorrente(cliente);
		return contaRepository.save(conta);
	}
	
	public ContaPoupanca addContaPoupanca(Long id_cliente) {
		//VALIDAR SE CLIENTE EXISTE
		Cliente cliente = clienteService.getClienteById(id_cliente);
		ContaPoupanca conta = new ContaPoupanca(cliente);
		return contaRepository.save(conta);
	}
	
	//get contas
	public List<ContaResponse> getContas(){
		List<ContaBase> contas = contaRepository.findAll();
		return contas.stream()
				.map(this::toResponse)
				.toList();
	}
	
	//get conta por cliente
	public List<ContaResponse> listarPorCliente(Long id_cliente){
		List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);
		return contas.stream()
				.map(this::toResponse)
				.toList();
	}
	
	//get uma conta
	public ContaResponse getContaById(Long id_conta) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_conta));
		return toResponse(conta);
	}
	
	public ContaCorrente getContaCorrenteById(Long id_conta) {
		return (ContaCorrente) contaRepository.findById(id_conta)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_conta));
	}
	public ContaPoupanca getContaPoupancaById(Long id_conta) {
		return (ContaPoupanca) contaRepository.findById(id_conta)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_conta));
	}
	
	//transferencia
	@Transactional
	public void transferir(Long id_contaOrigem, Long id_contaDestino, BigDecimal valor) {
		ContaBase origem = contaRepository.findById(id_contaOrigem)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_contaOrigem));
		ContaBase destino = contaRepository.findById(id_contaDestino)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_contaDestino));
		
		origem.transferir(destino, valor);
		
		contaRepository.save(origem);
		contaRepository.save(destino);
		
	    // Verificar saldo pós-operação
//	    if (origem.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
//	        throw new IllegalStateException("Saldo negativo após transferência");
//	        return false;
//	    }
		
//		// Registrar a transação
//		Transacao transacao = new Transacao(this, (ContaBase) destino, valor);
//		transacaoRepository.save(transacao);
	}
	
	//pix
	@Transactional
	public void pix(Long id_contaOrigem, Long id_contaDestino, BigDecimal valor) throws SaldoInsuficienteException {
		ContaBase origem = contaRepository.findById(id_contaOrigem)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_contaOrigem));
		ContaBase destino = contaRepository.findById(id_contaDestino)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_contaDestino));
		
		origem.pix(destino, valor);
		
		contaRepository.save(origem);
		contaRepository.save(destino);
		
	    // Verificar saldo pós-operação
//	    if (origem.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
//	        throw new IllegalStateException("Saldo negativo após transferência");
//	    }
		
//		// Registrar a transação
//		Transacao transacao = new Transacao(this, (ContaBase) destino, valor);
//		transacaoRepository.save(transacao);
	}
	
	//get saldo
	public SaldoResponse getSaldo(Long id_conta) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_conta));
		return toSaldoResponse(conta);
	}
	
	//deposito
	public void depositar(Long id_conta, BigDecimal valor) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_conta));
		conta.depositar(valor);
		contaRepository.save(conta);
		
//		// Registrar a transação
//		Transacao transacao = new Transacao(this, (ContaBase) destino, valor);
//		transacaoRepository.save(transacao);
	}
	
	//saque
	public void sacar(Long id_conta, BigDecimal valor) throws SaldoInsuficienteException {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(()-> new ContaNaoEncontradaException(id_conta));
		conta.sacar(valor);
		contaRepository.save(conta);
		
//		// Registrar a transação
//		Transacao transacao = new Transacao(this, (ContaBase) destino, valor);
//		transacaoRepository.save(transacao);
	}
	
	//txmanutencao
	public void debitarTaxaManutencao() {
		//TODO
	}
	
	//rendimento
	public void creditarRendimento() {
		//TODO
	}
	
	public ContaResponse toResponse(ContaBase conta) {
//		ContaResponse response = new ContaResponse (
//				conta.getId(),
//				conta.getNumeroConta(),
//				conta.getTipoConta(),
//				conta.getCliente().getId(),
//				conta.getMoeda(),
//				conta.getDataCriacao());
		
//	    // Adiciona campos específicos baseados no tipo de conta
//	    if (conta instanceof ContaCorrente cc) {
//	        response.setTaxaManutencao(cc.getTaxaManutencao());
//	    } else if (conta instanceof ContaPoupanca cp) {
//	        response.setTaxaRendimento(cp.getTaxaRendimento());
//	    }
		
		return ContaResponse.fromContaBase(conta);
	}
	
	public SaldoResponse toSaldoResponse(ContaBase conta) {
		
		return SaldoResponse.fromContaBase(conta);
	}
}
