package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.exceptions.ClienteNaoEncontradoException;
import br.com.cdb.bancodigitaljpa.exceptions.ContaNaoEncontradaException;
import br.com.cdb.bancodigitaljpa.exceptions.SaldoInsuficienteException;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;

@Service
public class ContaService {
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	//addConta de forma genérica
	
	public ContaCorrente addContaCorrente(Long clienteId) {
		//VALIDAR SE CLIENTE EXISTE
		Cliente cliente = clienteService.getClienteById(clienteId);
		ContaCorrente conta = new ContaCorrente(cliente);
		return contaRepository.save(conta);
	}
	
	public ContaPoupanca addContaPoupanca(Long clienteId) {
		//VALIDAR SE CLIENTE EXISTE
		Cliente cliente = clienteService.getClienteById(clienteId);
		ContaPoupanca conta = new ContaPoupanca(cliente);
		return contaRepository.save(conta);
	}
	
	//get contas
	public List<ContaBase> getContas(){
		System.out.println(contaRepository.findAll());
		return contaRepository.findAll();
	}
	
	//get uma conta
	public ContaCorrente getContaCorrenteById(Long id) {
		return (ContaCorrente) contaRepository.findById(id)
				.orElseThrow(()-> new ContaNaoEncontradaException(id));
	}
	public ContaPoupanca getContaPoupancaById(Long id) {
		return (ContaPoupanca) contaRepository.findById(id)
				.orElseThrow(()-> new ContaNaoEncontradaException(id));
	}
	
	//transferencia
	@Transactional
	public void transferir(Long contaOrigemId, Long contaDestinoId, @Positive BigDecimal valor) throws SaldoInsuficienteException {
		ContaBase origem = contaRepository.findById(contaOrigemId).orElseThrow(()-> new ClienteNaoEncontradoException(contaOrigemId));
		ContaBase destino = contaRepository.findById(contaDestinoId).orElseThrow(()-> new ClienteNaoEncontradoException(contaDestinoId));
		
		origem.sacar(valor);
		destino.depositar(valor);
		
		contaRepository.save(origem);
		contaRepository.save(destino);
		
	    // Verificar saldo pós-operação
	    if (origem.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
	        throw new IllegalStateException("Saldo negativo após transferência");
	    }
	}
	
	//pix
	@Transactional
	public void pix(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) throws SaldoInsuficienteException {
		ContaBase origem = contaRepository.findById(contaOrigemId).orElseThrow(()-> new ClienteNaoEncontradoException(contaOrigemId));
		ContaBase destino = contaRepository.findById(contaDestinoId).orElseThrow(()-> new ClienteNaoEncontradoException(contaDestinoId));
		
		origem.sacar(valor);
		destino.depositar(valor);
		
		contaRepository.save(origem);
		contaRepository.save(destino);
		
	    // Verificar saldo pós-operação
	    if (origem.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
	        throw new IllegalStateException("Saldo negativo após transferência");
	    }
	}
	
	//get saldo
	public BigDecimal getSaldo(Long id) {
		ContaBase conta = contaRepository.findById(id)
				.orElseThrow(()-> new ContaNaoEncontradaException(id));
		return conta.getSaldo();
	}
	
	//deposito
	public void depositar(Long id, BigDecimal valor) {
		ContaBase conta = contaRepository.findById(id)
				.orElseThrow(()-> new ContaNaoEncontradaException(id));
		conta.depositar(valor);
		contaRepository.save(conta);
	}
	
	//saque
	public void sacar(Long id, BigDecimal valor) throws SaldoInsuficienteException {
		ContaBase conta = contaRepository.findById(id)
				.orElseThrow(()-> new ContaNaoEncontradaException(id));
		conta.sacar(valor);
		contaRepository.save(conta);
	}
	
	//txmanutencao
	public void debitarTaxaManutencao() {
		//TODO
	}
	
	//rendimento
	public void creditarRendimento() {
		//TODO
	}
}
