package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoCartao;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.exceptions.ErrorMessages;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dao.CartaoDAO;
import br.com.cdb.bancodigital.dao.ClienteDAO;
import br.com.cdb.bancodigital.dao.ContaDAO;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.dao.SeguroDAO;
import br.com.cdb.bancodigital.dao.UsuarioRepository;
import br.com.cdb.bancodigital.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.dto.response.ContaResponse;
import br.com.cdb.bancodigital.dto.response.SeguroResponse;

@Service
public class AdminService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ClienteDAO clienteRepository;

	@Autowired
	private ContaDAO contaRepository;

	@Autowired
	private CartaoDAO cartaoRepository;

	@Autowired
	private SeguroDAO seguroRepository;
	
	@Autowired
	private PoliticaDeTaxasDAO politicaDeTaxaRepository;
	
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
		
		Usuario usuario = usuarioRepository.criarUsuario(dto.getEmail(), dto.getSenha(), dto.getRole());

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
		Conta contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
		contaRepository.save(contaNova);
		
		return toContaResponse(contaNova);
	}
	
	// add cartao
	@Transactional
	public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		Objects.requireNonNull(senha, "A senha do cartão não pode ser nula");

		Conta conta = verificarContaExitente(id_conta);
		securityService.validateAccess(usuarioLogado, conta.getCliente());
		Cartao cartaoNovo = criarCartaoPorTipo(tipo, conta, senha);
		cartaoRepository.save(cartaoNovo);

		return toCartaoResponse(cartaoNovo);
	}
	
	// contrataSeguro
	@Transactional
	public SeguroResponse contratarSeguro(Long id_cartao, Usuario usuarioLogado, TipoSeguro tipo) {
		Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
		Cartao ccr = cartaoRepository.findCartaoById(id_cartao)
				.orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
		securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());
		
		Seguro seguroNovo = contratarSeguroPorTipo(tipo, ccr);
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
	
	public Conta verificarContaExitente(Long id_conta) {
		Conta conta = contaRepository.findById(id_conta)
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

	public PoliticaDeTaxas verificarPolitiaExitente(CategoriaCliente categoria) {
		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
		.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
		return parametros;
	}
	
	public ContaResponse toContaResponse(Conta conta) {
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
	
	public CartaoResponse toCartaoResponse(Cartao cartao) {
		return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
				cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(), cartao.getLimite());
	}
	
	public Cartao criarCartaoPorTipo(TipoCartao tipo, Conta conta, String senha) {

		CategoriaCliente categoria = conta.getCliente().getCategoria();
		PoliticaDeTaxas parametros = verificarPolitiaExitente(categoria);
		
		return switch (tipo) {
		case CREDITO -> {
			Cartao ccr = new Cartao(conta, senha, tipo);
			ccr.setLimite(parametros.getLimiteCartaoCredito());
			yield ccr;
		}
		case DEBITO -> {
			Cartao cdb = new Cartao(conta, senha, tipo);
			cdb.setLimite(parametros.getLimiteDiarioDebito());
			yield cdb;
		}
		};

	}
	
	public Seguro contratarSeguroPorTipo(TipoSeguro tipo, Cartao ccr) {
		
		CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();

		PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(categoria)
				.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));

		return switch (tipo) {
		case FRAUDE -> {
			Seguro sf = new Seguro(ccr);
			sf.setValorApolice(parametros.getValorApoliceFraude());
			sf.setPremioApolice(parametros.getTarifaSeguroFraude());
			sf.setDescricaoCondicoes(TipoSeguro.FRAUDE.getDescricao());
			yield sf;
		}
		case VIAGEM -> {
			Seguro sv = new Seguro(ccr);
			sv.setValorApolice(parametros.getValorApoliceViagem());
			sv.setPremioApolice(parametros.getTarifaSeguroViagem());
			sv.setDescricaoCondicoes(TipoSeguro.VIAGEM.getDescricao());
			yield sv;
		}
		};
	}
	
	public SeguroResponse toSeguroResponse(Seguro seguro) {
		return SeguroResponse.toSeguroResponse(seguro);
	}
}
