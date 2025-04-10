package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaInternacional;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.exceptions.ErrorMessages;
import br.com.cdb.bancodigitaljpa.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.repository.ClienteRepository;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.response.AplicarTxManutencaoResponse;
import br.com.cdb.bancodigitaljpa.response.AplicarTxRendimentoResponse;
import br.com.cdb.bancodigitaljpa.response.ContaResponse;
import br.com.cdb.bancodigitaljpa.response.DepositoResponse;
import br.com.cdb.bancodigitaljpa.response.PixResponse;
import br.com.cdb.bancodigitaljpa.response.SaldoResponse;
import br.com.cdb.bancodigitaljpa.response.SaqueResponse;
import br.com.cdb.bancodigitaljpa.response.TransferenciaResponse;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;
	
	@Autowired
	private ConversorMoedasService conversorMoedasService;	

	// addConta de forma genérica
	@Transactional
	public ContaBase abrirConta(Long id_cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {

		Objects.requireNonNull(tipo, "Tipo de conta não pode ser nulo");

		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));

		ContaBase contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);

		return contaRepository.save(contaNova);
	}

	private ContaBase criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {

		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(cliente.getCategoria())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Parâmetros não encontrados para a categoria: " + cliente.getCategoria()));

		return switch (tipo) {
		case CORRENTE -> {
			yield criarContaCorrente(cliente, parametros);
		}
		case POUPANCA -> {
			yield criarContaPoupanca(cliente, parametros);
		}
		case INTERNACIONAL -> {	
			yield criarContaInternacional(cliente, moeda, valorDeposito, parametros);
		}
		};
	}
	
	private ContaCorrente criarContaCorrente(Cliente cliente, PoliticaDeTaxas parametros) {
		ContaCorrente cc = new ContaCorrente(cliente);
		cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
		return cc;
	}
	
	private ContaPoupanca criarContaPoupanca(Cliente cliente, PoliticaDeTaxas parametros) {
		ContaPoupanca cp = new ContaPoupanca(cliente);
		cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
		return cp;
	}
	
	private ContaInternacional criarContaInternacional(Cliente cliente, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
		ContaInternacional ci = new ContaInternacional(cliente, moeda, valorDeposito);
		ci.setTarifaManutencao(parametros.getTarifaManutencaoContaInternacional());
		BigDecimal saldoEmReais = conversorMoedasService.converterParaBrl(ci.getMoeda(), ci.getSaldo());
		ci.setSaldoEmReais(saldoEmReais);
		return ci;
	}

	// get contas
	public List<ContaResponse> getContas() {
		List<ContaBase> contas = contaRepository.findAll();
		return contas.stream().map(this::toResponse).toList();
	}

	// get conta por cliente
	public List<ContaResponse> listarPorCliente(Long id_cliente) {
		clienteRepository.findById(id_cliente)
		.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
		List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);
		return contas.stream().map(this::toResponse).toList();
	}

	// get uma conta
	public ContaResponse getContaById(Long id_conta) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
		return toResponse(conta);
	}

	// transferencia
	@Transactional
	public TransferenciaResponse transferir(Long id_contaOrigem, Long id_contaDestino, BigDecimal valor) {
		ContaBase origem = contaRepository.findById(id_contaOrigem).orElseThrow(
				() -> new ResourceNotFoundException("Conta com ID " + id_contaOrigem + " não encontrada."));
		ContaBase destino = contaRepository.findById(id_contaDestino).orElseThrow(
				() -> new ResourceNotFoundException("Conta com ID " + id_contaDestino + " não encontrada."));

		if (valor.compareTo(origem.getSaldo()) > 0)
			throw new InvalidInputParameterException("Saldo insuficiente para esta transação.");

		origem.transferir(destino, valor);

		contaRepository.save(origem);
		contaRepository.save(destino);

		return TransferenciaResponse.toTransferenciaResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);

		// Registrar a transação
	}

	// pix
	@Transactional
	public PixResponse pix(Long id_contaOrigem, Long id_contaDestino, BigDecimal valor) {
		ContaBase origem = contaRepository.findById(id_contaOrigem).orElseThrow(
				() -> new ResourceNotFoundException("Conta com ID " + id_contaOrigem + " não encontrada."));
		ContaBase destino = contaRepository.findById(id_contaDestino).orElseThrow(
				() -> new ResourceNotFoundException("Conta com ID " + id_contaDestino + " não encontrada."));

		if (valor.compareTo(origem.getSaldo()) > 0)
			throw new InvalidInputParameterException("Saldo insuficiente para esta transação.");

		origem.pix(destino, valor);

		contaRepository.save(origem);
		contaRepository.save(destino);

		return PixResponse.toPixResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);

//		// Registrar a transação
	}

	// get saldo
	public SaldoResponse getSaldo(Long id_conta) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
		return SaldoResponse.toSaldoResponse(conta);
	}

	// deposito
	@Transactional
	public DepositoResponse depositar(Long id_conta, BigDecimal valor) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
		
		conta.depositar(valor);
		contaRepository.save(conta);
		return DepositoResponse.toDepositoResponse(conta.getNumeroConta(), valor, conta.getSaldo());

//		// Registrar a transação

	}

	// saque
	@Transactional
	public SaqueResponse sacar(Long id_conta, BigDecimal valor) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));

		if (valor.compareTo(conta.getSaldo()) > 0)
			throw new InvalidInputParameterException("Saldo insuficiente para esta transação.");

		conta.sacar(valor);
		contaRepository.save(conta);

		return SaqueResponse.toSaqueResponse(conta.getNumeroConta(), valor, conta.getSaldo());

//		// Registrar a transação
	}

	// txmanutencao
	@Transactional
	public AplicarTxManutencaoResponse debitarTarifaManutencao(Long id_conta) {
		ContaCorrente cc = contaRepository.findContaCorrenteById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
		
		if (cc.getTarifaManutencao().compareTo(cc.getSaldo()) > 0)
			throw new InvalidInputParameterException("Saldo insuficiente para esta transação.");

		cc.aplicarTxManutencao();	
		contaRepository.save(cc);

		return AplicarTxManutencaoResponse.toAplicarTxManutencaoResponse(cc.getNumeroConta(), cc.getTarifaManutencao(), cc.getSaldo());
	}

	// rendimento
	@Transactional
	public AplicarTxRendimentoResponse creditarRendimento(Long id_conta) {
		ContaPoupanca cp = contaRepository.findContaPoupancaById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
		
		cp.aplicarRendimento();
		contaRepository.save(cp);
		
		return AplicarTxRendimentoResponse.toAplicarTxRendimentoResponse(cp.getNumeroConta(), cp.getTaxaRendimento(), cp.getSaldo());

	}

	public ContaResponse toResponse(ContaBase conta) {
		ContaResponse response = new ContaResponse(conta.getId(), conta.getNumeroConta(), conta.getTipoConta(),
				conta.getCliente().getId(), conta.getMoeda(), conta.getSaldo(), conta.getDataCriacao(),
				(conta instanceof ContaCorrente) ? ((ContaCorrente) conta).getTarifaManutencao()
						: (conta instanceof ContaPoupanca) ? ((ContaPoupanca) conta).getTaxaRendimento() 
								: (conta instanceof ContaInternacional) ? ((ContaInternacional) conta).getTarifaManutencao() : null);
		if (conta instanceof ContaInternacional) {
			ContaInternacional contaInt = (ContaInternacional) conta;
			response.setSaldoEmReais(contaInt.getSaldoEmReais());
		}
		
		return response;

	}

}
