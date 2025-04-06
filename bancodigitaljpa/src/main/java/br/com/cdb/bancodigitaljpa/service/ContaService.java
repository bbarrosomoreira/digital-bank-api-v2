package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigitaljpa.dto.ContaResponse;
import br.com.cdb.bancodigitaljpa.dto.SaldoResponse;
import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.exceptions.ClienteNaoEncontradoException;
import br.com.cdb.bancodigitaljpa.exceptions.ContaNaoEncontradaException;
import br.com.cdb.bancodigitaljpa.exceptions.SaldoInsuficienteException;
import br.com.cdb.bancodigitaljpa.exceptions.TipoContaInvalidoException;
import br.com.cdb.bancodigitaljpa.repository.ClienteRepository;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;

@Service
public class ContaService {
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;
	
	//addConta de forma genérica
	@Transactional
	public ContaBase abrirConta(Long id_cliente, TipoConta tipo) {
		
		Objects.requireNonNull(tipo, "Tipo de conta não pode ser nulo");
		
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id_cliente));	
		
		ContaBase contaNova = criarContaPorTipo(tipo, cliente);
		
		return contaRepository.save(contaNova);			
	}
	
	private ContaBase criarContaPorTipo(TipoConta tipo, Cliente cliente) {
		
		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(cliente.getCategoria()).orElseThrow(
				() -> new RuntimeException("Parâmetros não encontrados para a categoria: " + cliente.getCategoria()));
		
		return switch(tipo) {
			case CORRENTE -> {
				ContaCorrente cc = new ContaCorrente(cliente);
				cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
				yield cc; // retorno de valor
			}
			case POUPANCA -> {
				ContaPoupanca cp = new ContaPoupanca(cliente);
				cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
				yield cp;
			}
		};
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
		//ADD VALIDACAO DE ID CLIENTE?
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
	@Transactional
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
	@Transactional
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
	@Transactional
	public void debitarTarifaManutencao(Long id_conta) {
		ContaCorrente cc = contaRepository.findContaCorrenteById(id_conta)
				.orElseThrow(()-> new TipoContaInvalidoException("Conta corrente não encontrada com ID: "+ id_conta));
		BigDecimal taxaMensal = cc.getTarifaManutencao();
		BigDecimal saldoAtual = cc.getSaldo();
		if (taxaMensal.compareTo(saldoAtual) > 0) {
			throw new SaldoInsuficienteException(cc.getId(), cc.getNumeroConta(), cc.getSaldo());
		}
		else {
			BigDecimal saldoNovo = saldoAtual.subtract(taxaMensal);
			cc.setSaldo(saldoNovo);
			contaRepository.save(cc);
		}
	}
	
	//rendimento
	@Transactional
	public void creditarRendimento(Long id_conta) {
		ContaPoupanca cp = contaRepository.findContaPoupancaById(id_conta)
				.orElseThrow(()-> new TipoContaInvalidoException("Conta poupança não encontrada com ID: "+ id_conta));
		BigDecimal taxaMensal = cp.getTaxaRendimento();
		BigDecimal saldoAtual = cp.getSaldo();
		BigDecimal rendimento = saldoAtual.multiply(taxaMensal);
		BigDecimal saldoNovo = saldoAtual.add(rendimento);
		cp.setSaldo(saldoNovo);
		contaRepository.save(cp);
		
	}
	
	public ContaResponse toResponse(ContaBase conta) {		
		return new ContaResponse (
				conta.getId(),
				conta.getNumeroConta(),
				conta.getTipoConta(),
				conta.getCliente().getId(),
				conta.getMoeda(),
				conta.getDataCriacao(),
				(conta instanceof ContaCorrente) ? ((ContaCorrente) conta).getTarifaManutencao() :
					(conta instanceof ContaPoupanca) ? ((ContaPoupanca) conta).getTaxaRendimento() : null);
	
	}
	
	public SaldoResponse toSaldoResponse(ContaBase conta) {	
		return SaldoResponse.fromContaBase(conta);
	}

}
