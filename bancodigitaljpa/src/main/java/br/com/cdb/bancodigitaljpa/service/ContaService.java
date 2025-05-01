package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigitaljpa.model.Cliente;
import br.com.cdb.bancodigitaljpa.model.ContaBase;
import br.com.cdb.bancodigitaljpa.model.ContaCorrente;
import br.com.cdb.bancodigitaljpa.model.ContaInternacional;
import br.com.cdb.bancodigitaljpa.model.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.model.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.model.Usuario;
import br.com.cdb.bancodigitaljpa.model.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.model.enums.Moeda;
import br.com.cdb.bancodigitaljpa.model.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.exceptions.ErrorMessages;
import br.com.cdb.bancodigitaljpa.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ValidationException;
import br.com.cdb.bancodigitaljpa.dao.CartaoRepository;
import br.com.cdb.bancodigitaljpa.dao.ClienteRepository;
import br.com.cdb.bancodigitaljpa.dao.ContaRepository;
import br.com.cdb.bancodigitaljpa.dao.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.dto.response.AplicarTxManutencaoResponse;
import br.com.cdb.bancodigitaljpa.dto.response.AplicarTxRendimentoResponse;
import br.com.cdb.bancodigitaljpa.dto.response.ContaResponse;
import br.com.cdb.bancodigitaljpa.dto.response.DepositoResponse;
import br.com.cdb.bancodigitaljpa.dto.response.PixResponse;
import br.com.cdb.bancodigitaljpa.dto.response.SaldoResponse;
import br.com.cdb.bancodigitaljpa.dto.response.SaqueResponse;
import br.com.cdb.bancodigitaljpa.dto.response.TransferenciaResponse;

@Service
public class ContaService {
	
	private static final Logger log = LoggerFactory.getLogger(CartaoService.class);

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;
	
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
		ContaBase contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
		contaRepository.save(contaNova);
		
		return toResponse(contaNova);
	}

	private ContaBase criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
		PoliticaDeTaxas parametros = verificarPolitiaExitente(cliente.getCategoria());

		return switch (tipo) {
		case CORRENTE -> {
			yield criarContaCorrente(cliente, moeda, valorDeposito, parametros);
		}
		case POUPANCA -> {
			yield criarContaPoupanca(cliente, moeda, valorDeposito, parametros);
		}
		case INTERNACIONAL -> {	
			yield criarContaInternacional(cliente, moeda, valorDeposito, parametros);
		}
		};
	}
	
	private ContaCorrente criarContaCorrente(Cliente cliente, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
		ContaCorrente cc = new ContaCorrente();
		cc.setCliente(cliente);
		cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
		cc.setMoeda(moeda);
		cc.setSaldo(valorDeposito);
		return cc;
	}
	
	private ContaPoupanca criarContaPoupanca(Cliente cliente, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
		ContaPoupanca cp = new ContaPoupanca();
		cp.setCliente(cliente);
		cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
		cp.setMoeda(moeda);
		cp.setSaldo(valorDeposito);
		return cp;
	}
	
	private ContaInternacional criarContaInternacional(Cliente cliente, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
		ContaInternacional ci = new ContaInternacional(cliente, moeda, valorDeposito);
		ci.setTarifaManutencao(parametros.getTarifaManutencaoContaInternacional());
		BigDecimal saldoMoedaExtrangeira = conversorMoedasService.converterDeBrl(ci.getMoeda(), ci.getSaldoEmReais());
		ci.setSaldo(saldoMoedaExtrangeira);
		return ci;
	}

	// get contas
	public List<ContaResponse> getContas() {
		List<ContaBase> contas = contaRepository.findAll();
		return contas.stream().map(this::toResponse).toList();
	}

	// get conta por usuário
	public List<ContaResponse> listarPorUsuario(Usuario usuarioLogado) {
		List<ContaBase> contas = contaRepository.findByClienteUsuario(usuarioLogado);
		return contas.stream().map(this::toResponse).toList();
	}
	
	// get conta por cliente
	public List<ContaResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);
		return contas.stream().map(this::toResponse).toList();
	}

	// get uma conta
	public ContaResponse getContaById(Long id_conta, Usuario usuarioLogado) {
		ContaBase conta = verificarContaExitente(id_conta);
		Cliente cliente = conta.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		return toResponse(conta);
	}
	
	// deletar contas de cliente
	@Transactional
	public void deleteContasByCliente(Long id_cliente) {
		List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);
		if (contas.isEmpty()) {
			log.info("Cliente Id {} não possui contas.", id_cliente);
			return;
		}
		for (ContaBase conta : contas) {
			try {
				verificarCartoesVinculados(conta);
				verificaSaldoRemanescente(conta);
				Long id = conta.getId();
				contaRepository.delete(conta);
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
		ContaBase origem = verificarContaExitente(id_contaOrigem);
		ContaBase destino = verificarContaExitente(id_contaDestino);
		Cliente cliente = origem.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		verificaSaldoSuficiente(valor, origem.getSaldo());
		origem.transferir(destino, valor);
		contaRepository.save(origem);
		contaRepository.save(destino);
		return TransferenciaResponse.toTransferenciaResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);

		// Registrar a transação
	}

	// pix
	@Transactional
	public PixResponse pix(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor) {
		ContaBase origem = verificarContaExitente(id_contaOrigem);
		ContaBase destino = verificarContaExitente(id_contaDestino);
		Cliente cliente = origem.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		verificaSaldoSuficiente(valor, origem.getSaldo());
		origem.pix(destino, valor);
		contaRepository.save(origem);
		contaRepository.save(destino);
		return PixResponse.toPixResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);

//		// Registrar a transação
	}

	// get saldo
	public SaldoResponse getSaldo(Long id_conta, Usuario usuarioLogado) {
		ContaBase conta = verificarContaExitente(id_conta);
		Cliente cliente = conta.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		return SaldoResponse.toSaldoResponse(conta);
	}

	// deposito
	@Transactional
	public DepositoResponse depositar(Long id_conta, BigDecimal valor) {
		ContaBase conta = verificarContaExitente(id_conta);
		conta.depositar(valor);
		contaRepository.save(conta);
		return DepositoResponse.toDepositoResponse(conta.getNumeroConta(), valor, conta.getSaldo());

//		// Registrar a transação

	}

	// saque
	@Transactional
	public SaqueResponse sacar(Long id_conta, Usuario usuarioLogado, BigDecimal valor) {
		ContaBase conta = verificarContaExitente(id_conta);
		Cliente cliente = conta.getCliente();
		securityService.validateAccess(usuarioLogado, cliente);
		verificaSaldoSuficiente(valor, conta.getSaldo());
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
			
		verificaSaldoSuficiente(cc.getTarifaManutencao(), cc.getSaldo());
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
	
	//M
	public ContaResponse toResponse(ContaBase conta) {
		ContaResponse response = new ContaResponse(conta.getId(), conta.getNumeroConta(), conta.getTipoConta(),
				 conta.getMoeda(), conta.getSaldo(), conta.getDataCriacao(),
				(conta instanceof ContaCorrente) ? ((ContaCorrente) conta).getTarifaManutencao()
						: (conta instanceof ContaPoupanca) ? ((ContaPoupanca) conta).getTaxaRendimento() 
								: (conta instanceof ContaInternacional) ? ((ContaInternacional) conta).getTarifaManutencao() : null);
		if (conta instanceof ContaInternacional) {
			ContaInternacional contaInt = (ContaInternacional) conta;
			response.setSaldoEmReais(contaInt.getSaldoEmReais());
		}	
		return response;
	}
	public void verificaSaldoRemanescente(ContaBase conta) {
		if(conta.getSaldo() != null && conta.getSaldo().compareTo(BigDecimal.ZERO)>0) throw new InvalidInputParameterException("Não é possivel excluir uma conta com saldo remanescente.");	
	}
	public void verificarCartoesVinculados(ContaBase conta) {
		if(cartaoRepository.existsByContaId(conta.getId())) throw new InvalidInputParameterException("Conta não pode ser excluída com cartões vinculados.");
	}
	public ContaBase verificarContaExitente(Long id_conta) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID "+id_conta+" não encontrada."));
		return conta;
	}
	public PoliticaDeTaxas verificarPolitiaExitente(CategoriaCliente categoria) {
		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
		.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
		return parametros;
	}
	public Cliente verificarClienteExistente(Long id_cliente) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
		return cliente;
	}
	public void verificaSaldoSuficiente(BigDecimal valor, BigDecimal saldo) {
		if (valor.compareTo(saldo) > 0)
			throw new InvalidInputParameterException("Saldo insuficiente para esta transação.");
	}

}
