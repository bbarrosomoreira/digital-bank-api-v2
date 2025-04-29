package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigitaljpa.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigitaljpa.model.CartaoBase;
import br.com.cdb.bancodigitaljpa.model.CartaoCredito;
import br.com.cdb.bancodigitaljpa.model.CartaoDebito;
import br.com.cdb.bancodigitaljpa.model.Cliente;
import br.com.cdb.bancodigitaljpa.model.ContaBase;
import br.com.cdb.bancodigitaljpa.model.ContaCorrente;
import br.com.cdb.bancodigitaljpa.model.ContaInternacional;
import br.com.cdb.bancodigitaljpa.model.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.model.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.model.SeguroBase;
import br.com.cdb.bancodigitaljpa.model.SeguroFraude;
import br.com.cdb.bancodigitaljpa.model.SeguroViagem;
import br.com.cdb.bancodigitaljpa.model.Usuario;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoCartao;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import br.com.cdb.bancodigitaljpa.exceptions.ErrorMessages;
import br.com.cdb.bancodigitaljpa.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ValidationException;
import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
import br.com.cdb.bancodigitaljpa.repository.ClienteRepository;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.repository.SeguroRepository;
import br.com.cdb.bancodigitaljpa.repository.UsuarioRepository;
import br.com.cdb.bancodigitaljpa.response.CartaoResponse;
import br.com.cdb.bancodigitaljpa.response.ClienteResponse;
import br.com.cdb.bancodigitaljpa.response.ContaResponse;
import br.com.cdb.bancodigitaljpa.response.SeguroResponse;

@Service
public class AdminService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private SeguroRepository seguroRepository;
	
	@Autowired
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private ReceitaService receitaService;
	
	@Autowired
    private BrasilApiService brasilApiService;
	
	@Autowired
	private ConversorMoedasService conversorMoedasService;	
	
	
	// Cadastrar cliente
	public ClienteResponse cadastrarCliente(ClienteUsuarioDTO dto) {
		CEP2 cepInfo = brasilApiService.buscarEnderecoPorCep(dto.getCep());
		
		Usuario usuario = usuarioRepository.inserirUsuario(dto.getEmail(), dto.getSenha(), dto.getRole());

		Cliente cliente = dto.transformaParaClienteObjeto();
		cliente.getEndereco().setCep(dto.getCep());
		cliente.getEndereco().setBairro(cepInfo.getNeighborhood());
		cliente.getEndereco().setCidade(cepInfo.getCity());
		cliente.getEndereco().setComplemento(dto.getComplemento());
		cliente.getEndereco().setEnderecoPrincipal(true);
		cliente.getEndereco().setEstado(cepInfo.getState());
		cliente.getEndereco().setNumero(dto.getNumero());
		cliente.getEndereco().setRua(cepInfo.getStreet());

		cliente.setUsuario(usuario);
		
		if(!receitaService.isCpfValidoEAtivo(cliente.getCpf())) throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");
		
		validarCpfUnico(cliente.getCpf());
		validarMaiorIdade(cliente);

		clienteRepository.save(cliente);
		return toClienteResponse(cliente);
	}
	
	// addConta de forma genérica
	@Transactional
	public ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {
		Objects.requireNonNull(tipo, "Tipo de conta não pode ser nulo");

		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		ContaBase contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
		contaRepository.save(contaNova);
		
		return toContaResponse(contaNova);
	}
	
	// add cartao
	@Transactional
	public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		Objects.requireNonNull(senha, "A senha do cartão não pode ser nula");

		ContaBase conta = verificarContaExitente(id_conta);
		securityService.validateAccess(usuarioLogado, conta.getCliente());
		CartaoBase cartaoNovo = criarCartaoPorTipo(tipo, conta, senha);
		cartaoRepository.save(cartaoNovo);

		return toCartaoResponse(cartaoNovo);
	}
	
	// contrataSeguro
	@Transactional
	public SeguroResponse contratarSeguro(Long id_cartaoCredito, Usuario usuarioLogado, TipoSeguro tipo) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		CartaoCredito ccr = cartaoRepository.findCartaoCreditoById(id_cartaoCredito)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartaoCredito + " não encontrado."));
		securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
		
		SeguroBase seguroNovo = contratarSeguroPorTipo(tipo, ccr);
		seguroRepository.save(seguroNovo);
		return toSeguroResponse(seguroNovo);
	}
	
	public Cliente getClienteById(Long id_cliente, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		return cliente;
	}
	
	public Cliente verificarClienteExistente(Long id_cliente) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
		return cliente;
	}
	
	public ContaBase verificarContaExitente(Long id_conta) {
		ContaBase conta = contaRepository.findById(id_conta)
				.orElseThrow(() -> new ResourceNotFoundException("Conta com ID "+id_conta+" não encontrada."));
		return conta;
	}
	
	public ClienteResponse toClienteResponse(Cliente cliente) {
		return new ClienteResponse(cliente);
	}
	private void validarCpfUnico(String cpf) {
		if (clienteRepository.existsByCpf(cpf))
			throw new ResourceAlreadyExistsException("CPF já cadastrado no sistema.");
	}
	private void validarMaiorIdade(Cliente cliente) {
		if (!cliente.isMaiorDeIdade())
			throw new ValidationException("Cliente deve ser maior de 18 anos para se cadastrar.");
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
		ContaCorrente cc = new ContaCorrente(cliente);
		cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
		cc.setMoeda(moeda);
		cc.setSaldo(valorDeposito);
		return cc;
	}
	
	private ContaPoupanca criarContaPoupanca(Cliente cliente, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
		ContaPoupanca cp = new ContaPoupanca(cliente);
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

	public PoliticaDeTaxas verificarPolitiaExitente(CategoriaCliente categoria) {
		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
		.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
		return parametros;
	}
	
	public ContaResponse toContaResponse(ContaBase conta) {
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
	
	public CartaoResponse toCartaoResponse(CartaoBase cartao) {
		return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
				cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(),
				(cartao instanceof CartaoCredito) ? ((CartaoCredito) cartao).getLimiteCredito()
						: (cartao instanceof CartaoDebito) ? ((CartaoDebito) cartao).getLimiteDiario() : null);
	}
	
	public CartaoBase criarCartaoPorTipo(TipoCartao tipo, ContaBase conta, String senha) {

		CategoriaCliente categoria = conta.getCliente().getCategoria();
		PoliticaDeTaxas parametros = verificarPolitiaExitente(categoria);
		
		return switch (tipo) {
		case CREDITO -> {
			CartaoCredito ccr = new CartaoCredito(conta, senha, parametros.getLimiteCartaoCredito());
			yield ccr;
		}
		case DEBITO -> {
			CartaoDebito cdb = new CartaoDebito(conta, senha, parametros.getLimiteDiarioDebito());
			yield cdb;
		}
		};

	}
	
	public SeguroBase contratarSeguroPorTipo(TipoSeguro tipo, CartaoCredito ccr) {
		
		CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();

		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
				.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));

		return switch (tipo) {
		case FRAUDE -> {
			SeguroFraude sf = new SeguroFraude(ccr);
			sf.setPremioApolice(parametros.getTarifaSeguroFraude());
			yield sf;
		}
		case VIAGEM -> {
			SeguroViagem sv = new SeguroViagem(ccr);
			sv.setPremioApolice(parametros.getTarifaSeguroViagem());
			yield sv;
		}
		};
	}
	
	public SeguroResponse toSeguroResponse(SeguroBase seguro) {
		return SeguroResponse.toSeguroResponse(seguro);
	}
}
