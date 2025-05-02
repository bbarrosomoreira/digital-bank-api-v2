package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import br.com.cdb.bancodigital.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import br.com.cdb.bancodigital.exceptions.ErrorMessages;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dao.CartaoDAO;
import br.com.cdb.bancodigital.dao.ClienteDAO;
import br.com.cdb.bancodigital.dao.ContaDAO;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.dto.response.AplicarTxManutencaoResponse;
import br.com.cdb.bancodigital.dto.response.AplicarTxRendimentoResponse;
import br.com.cdb.bancodigital.dto.response.ContaResponse;
import br.com.cdb.bancodigital.dto.response.DepositoResponse;
import br.com.cdb.bancodigital.dto.response.PixResponse;
import br.com.cdb.bancodigital.dto.response.SaldoResponse;
import br.com.cdb.bancodigital.dto.response.SaqueResponse;
import br.com.cdb.bancodigital.dto.response.TransferenciaResponse;

@Service
public class ContaService {
	
	private static final Logger log = LoggerFactory.getLogger(ContaService.class);

	@Autowired
	private ContaDAO contaDAO;

	@Autowired
	private ClienteDAO clienteDAO;
	
	@Autowired
	private CartaoDAO cartaoDAO;

	@Autowired
	private PoliticaDeTaxasDAO politicaDeTaxaDAO;
	
	@Autowired
	private ConversorMoedasService conversorMoedasService;	
	
	@Autowired
	private SecurityService securityService;

	// addConta de forma genérica
	@Transactional
	public ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {
		Objects.requireNonNull(tipo, "Tipo de conta não pode ser nulo");

		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		Conta contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
		contaDAO.salvar(contaNova);
		
		return toResponse(contaNova);
	}

	private Conta criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
		PoliticaDeTaxas parametros = verificarPolitiaExitente(cliente.getCategoria());

		return switch (tipo) {
		case CORRENTE -> {
			yield criarContaCorrente(cliente, tipo, moeda, valorDeposito, parametros);
		}
		case POUPANCA -> {
			yield criarContaPoupanca(cliente, tipo, moeda, valorDeposito, parametros);
		}
		case INTERNACIONAL -> {	
			yield criarContaInternacional(cliente, tipo, moeda, valorDeposito, parametros);
		}
		};
	}

	private Conta criarContaCorrente(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
		Conta cc = new Conta(cliente, tipo);
		cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
		cc.setMoeda(moeda);
		cc.setSaldo(valorDeposito);
		return cc;
	}

	private Conta criarContaPoupanca(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
		Conta cp = new Conta(cliente, tipo);
		cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
		cp.setMoeda(moeda);
		cp.setSaldo(valorDeposito);
		return cp;
	}

	private Conta criarContaInternacional(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
		Conta ci = new Conta(cliente, tipo);
		ci.setTarifaManutencao(parametros.getTarifaManutencaoContaInternacional());
		ci.setMoeda(moeda);
		ci.setSaldoEmReais(valorDeposito);
		BigDecimal saldoMoedaExtrangeira = conversorMoedasService.converterDeBrl(ci.getMoeda(), ci.getSaldoEmReais());
		ci.setSaldo(saldoMoedaExtrangeira);
		return ci;
	}

	// get contas
	public List<ContaResponse> getContas() {
		List<Conta> contas = contaDAO.buscarTodasContas();
		return contas.stream().map(this::toResponse).toList();
	}

	// get conta por usuário
	public List<ContaResponse> listarPorUsuario(Usuario usuarioLogado) {
		List<Conta> contas = contaDAO.buscarContaPorClienteUsuario(usuarioLogado);
		return contas.stream().map(this::toResponse).toList();
	}
	
	// get conta por cliente
	public List<ContaResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		List<Conta> contas = contaDAO.buscarContaPorClienteId(id_cliente);
		return contas.stream().map(this::toResponse).toList();
	}

	// get uma conta
	public ContaResponse getContaById(Long id_conta, Usuario usuarioLogado) {
		Conta conta = verificarContaExitente(id_conta);
		Cliente cliente = conta.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		return toResponse(conta);
	}
	
	// deletar contas de cliente
	@Transactional
	public void deleteContasByCliente(Long id_cliente) {
		List<Conta> contas = contaDAO.buscarContaPorClienteId(id_cliente);
		if (contas.isEmpty()) {
			log.info("Cliente Id {} não possui contas.", id_cliente);
			return;
		}
		for (Conta conta : contas) {
			try {
				verificarCartoesVinculados(conta);
				verificaSaldoRemanescente(conta);
				Long id = conta.getId();
				contaDAO.deletarContaPorId(conta.getId());
				log.info("Conta ID {} deletado com sucesso", id);
				
			} catch (DataIntegrityViolationException e) {
	            log.error("Falha ao deletar conta ID {}", conta.getId(), e);
	            throw new ValidationException("Erro ao deletar cartão: " + e.getMessage());
	        }		
		}	
	}

	// transferencia
	@Transactional
	public TransferenciaResponse transferir(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor) {
		Conta origem = verificarContaExitente(id_contaOrigem);
		Conta destino = verificarContaExitente(id_contaDestino);
		Cliente cliente = origem.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		verificaSaldoSuficiente(valor, origem.getSaldo());
		origem.transferir(destino, valor);
		contaDAO.salvar(origem);
		contaDAO.salvar(destino);
		return TransferenciaResponse.toTransferenciaResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);

		// Registrar a transação
	}

	// pix
	@Transactional
	public PixResponse pix(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor) {
		Conta origem = verificarContaExitente(id_contaOrigem);
		Conta destino = verificarContaExitente(id_contaDestino);
		Cliente cliente = origem.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		verificaSaldoSuficiente(valor, origem.getSaldo());
		origem.pix(destino, valor);
		contaDAO.salvar(origem);
		contaDAO.salvar(destino);
		return PixResponse.toPixResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);

//		// Registrar a transação
	}

	// get saldo
	public SaldoResponse getSaldo(Long id_conta, Usuario usuarioLogado) {
		Conta conta = verificarContaExitente(id_conta);
		Cliente cliente = conta.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		return SaldoResponse.toSaldoResponse(conta);
	}

	// deposito
	@Transactional
	public DepositoResponse depositar(Long id_conta, BigDecimal valor) {
		Conta conta = verificarContaExitente(id_conta);
		conta.depositar(valor);
		contaDAO.salvar(conta);
		return DepositoResponse.toDepositoResponse(conta.getNumeroConta(), valor, conta.getSaldo());

//		// Registrar a transação

	}

	// saque
	@Transactional
	public SaqueResponse sacar(Long id_conta, Usuario usuarioLogado, BigDecimal valor) {
		Conta conta = verificarContaExitente(id_conta);
		Cliente cliente = conta.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		verificaSaldoSuficiente(valor, conta.getSaldo());
		conta.sacar(valor);
		contaDAO.salvar(conta);
		return SaqueResponse.toSaqueResponse(conta.getNumeroConta(), valor, conta.getSaldo());

//		// Registrar a transação
	}

	// txmanutencao
	@Transactional
	public AplicarTxManutencaoResponse debitarTarifaManutencao(Long id_conta) {
		Conta cc = contaDAO.buscarContaPorId(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
			
		verificaSaldoSuficiente(cc.getTarifaManutencao(), cc.getSaldo());
		cc.aplicarTarifaManutencao();
		contaDAO.salvar(cc);
		return AplicarTxManutencaoResponse.toAplicarTxManutencaoResponse(cc.getNumeroConta(), cc.getTarifaManutencao(), cc.getSaldo());
	}

	// rendimento
	@Transactional
	public AplicarTxRendimentoResponse creditarRendimento(Long id_conta) {
		Conta cp = contaDAO.buscarContaPorId(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
		
		cp.aplicarRendimento();
		contaDAO.salvar(cp);
		return AplicarTxRendimentoResponse.toAplicarTxRendimentoResponse(cp.getNumeroConta(), cp.getTaxaRendimento(), cp.getSaldo());

	}
	
	//M
	public ContaResponse toResponse(Conta conta) {
		BigDecimal tarifa;
		switch (conta.getTipoConta()){
			case CORRENTE, INTERNACIONAL -> {
				tarifa = conta.getTarifaManutencao();
			}
			case POUPANCA -> {
				tarifa = conta.getTaxaRendimento();
			}
			default -> throw new IllegalStateException("Unexpected value: " + conta.getTipoConta());
		}
		ContaResponse response = new ContaResponse(conta.getId(), conta.getNumeroConta(), conta.getTipoConta(),
				conta.getMoeda(), conta.getSaldo(), conta.getDataCriacao(),
				tarifa);
		if (conta.getTipoConta().equals(TipoConta.INTERNACIONAL)) {
			response.setSaldoEmReais(conta.getSaldoEmReais());
		}
		return response;
	}
	public void verificaSaldoRemanescente(Conta conta) {
		if(conta.getSaldo() != null && conta.getSaldo().compareTo(BigDecimal.ZERO)>0) throw new InvalidInputParameterException("Não é possivel excluir uma conta com saldo remanescente.");	
	}
	public void verificarCartoesVinculados(Conta conta) {
		if(cartaoDAO.existsByContaId(conta.getId())) throw new InvalidInputParameterException("Conta não pode ser excluída com cartões vinculados.");
	}
	public Conta verificarContaExitente(Long id_conta) {
		Conta conta = contaDAO.buscarContaPorId(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID "+id_conta+" não encontrada."));
		return conta;
	}
	public PoliticaDeTaxas verificarPolitiaExitente(CategoriaCliente categoria) {
		PoliticaDeTaxas parametros = politicaDeTaxaDAO.findByCategoria(categoria)
		.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
		return parametros;
	}
	public Cliente verificarClienteExistente(Long id_cliente) {
		Cliente cliente = clienteDAO.buscarClienteporId(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
		return cliente;
	}
	public void verificaSaldoSuficiente(BigDecimal valor, BigDecimal saldo) {
		if (valor.compareTo(saldo) > 0)
			throw new InvalidInputParameterException("Saldo insuficiente para esta transação.");
	}

}
