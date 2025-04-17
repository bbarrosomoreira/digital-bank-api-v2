package br.com.cdb.bancodigitaljpa.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigitaljpa.dto.ClienteDTO;
import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.CartaoDebito;
import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.entity.EnderecoCliente;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
import br.com.cdb.bancodigitaljpa.entity.SeguroFraude;
import br.com.cdb.bancodigitaljpa.entity.SeguroViagem;
import br.com.cdb.bancodigitaljpa.entity.Usuario;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
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
import br.com.cdb.bancodigitaljpa.response.ClienteResponse;

@Service
public class ClienteService {

	private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

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
	private ReceitaService receitaService;
	
	@Autowired
	private SecurityService securityService;

	// Cadastrar cliente
	public ClienteResponse cadastrarCliente(ClienteDTO  dto, Usuario usuario) {
		
		Cliente cliente = dto.transformaParaObjeto();
		cliente.setUsuario(usuario);
		
		if(!receitaService.isCpfValidoEAtivo(cliente.getCpf())) throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");
		
		validarCpfUnico(cliente.getCpf());
		validarMaiorIdade(cliente);

		clienteRepository.save(cliente);
		return toResponse(cliente);
	}

	// Ver cliente(s)
	public List<ClienteResponse> getClientes() throws AccessDeniedException { //só admin
		List<Cliente> clientes = clienteRepository.findAll();
		return clientes.stream().map(this::toResponse).toList();
	} 

	public ClienteResponse getClienteById(Long id_cliente, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		return toResponse(cliente);
	}
	public ClienteResponse buscarClienteDoUsuario(Usuario usuario) {
	    Cliente cliente = clienteRepository.findByUsuario(usuario)
	        .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o usuário logado."));
	    return toResponse(cliente);
	}

	// deletar cadastro de cliente
	@Transactional
	public void deleteCliente(Long id_cliente) throws AccessDeniedException { //só admin
		Cliente cliente = verificarClienteExistente(id_cliente);
		
		boolean temContas = contaRepository.existsByClienteId(id_cliente);
		boolean temCartoes = cartaoRepository.existsByContaClienteId(id_cliente);
		boolean temSeguros = seguroRepository.existsByCartaoCreditoContaClienteId(id_cliente);

		if (temContas || temCartoes || temSeguros) throw new ValidationException(
					"Cliente possui vínculos com contas, cartões ou seguros e não pode ser deletado.");

		clienteRepository.delete(cliente);
		log.info("Cliente ID {} deletado com sucesso", id_cliente);
	}

	// Atualizações de cliente
	@Transactional
	public ClienteResponse updateParcial(Long id_cliente, Map<String, Object> camposAtualizados, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		
		securityService.validateAccess(usuarioLogado, cliente);
		
		// atualizando apenas campos não nulos
		camposAtualizados.forEach((campo, valor) -> {
			switch (campo) {
			case "nome":
				if (valor != null) {
					cliente.setNome((String) valor);
				}
				break;
			case "cpf":
				if (valor != null) {
					cliente.setCpf((String) valor);
				}
				break;
			case "dataNascimento":
				if (valor != null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					cliente.setDataNascimento(LocalDate.parse((String) valor, formatter));
				}
				break;
			case "endereco":
				if (valor != null) {
					EnderecoCliente endereco = (EnderecoCliente) valor;
					cliente.setEndereco(endereco);
				}
				break;
			}
		});
		
		clienteRepository.save(cliente);
		return toResponse(cliente);
	}

	@Transactional
	public ClienteResponse updateCliente(Long id_cliente, ClienteDTO dto, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		
		securityService.validateAccess(usuarioLogado, cliente);
		
		// atualiza todos os campos
		cliente.setNome(dto.getNome());
		if (!cliente.getCpf().equals(dto.getCpf())) {
			validarCpfUnico(dto.getCpf());
		}
		cliente.setCpf(dto.getCpf());
		cliente.setDataNascimento(dto.getDataNascimento());
		cliente.setEndereco(dto.getEndereco());
		
		clienteRepository.save(cliente);
		return toResponse(cliente);
	}

	@Transactional
	public ClienteResponse updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) throws AccessDeniedException { // só admin
		Cliente cliente = verificarClienteExistente(id_cliente);
		if (cliente.getCategoria().equals(novaCategoria))
			throw new InvalidInputParameterException("Cliente ID " + id_cliente + " já está na categoria "
					+ novaCategoria + ". Nenhuma atualização necessária.");

		cliente.setCategoria(novaCategoria);

		atualizarTaxasDoCliente(id_cliente, novaCategoria);

		return toResponse(cliente);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void atualizarTaxasDoCliente(Long id_cliente, CategoriaCliente novaCategoria) {
		try {
			PoliticaDeTaxas parametros = verificarPolitiaExitente(novaCategoria);
			
			atualizarTaxasDasContas(id_cliente, parametros);
			atualizarTaxasDosCartoes(id_cliente, parametros);
			atualizarTaxasDosSeguros(id_cliente, parametros);

		} catch (Exception e) {
			log.error("Falha ao atualizar Política de Taxas do cliente ID {}", id_cliente, e);
			throw e;
		}
	}

	// M
	public ClienteResponse toResponse(Cliente cliente) {
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
	public void atualizarTaxasDasContas(Long id_cliente, PoliticaDeTaxas parametros) {
		List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);

		if (contas.isEmpty()) {
			log.info("Cliente ID {} não possui contas.", id_cliente);
			return;
		} else {
			contas.forEach(conta -> {
				if (conta instanceof ContaCorrente cc) {
					cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
				} else if (conta instanceof ContaPoupanca cp) {
					cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
				}
			});
			contaRepository.saveAll(contas);		
		}
	}
	public void atualizarTaxasDosCartoes(Long id_cliente, PoliticaDeTaxas parametros) {
		List<CartaoBase> cartoes = cartaoRepository.findByContaClienteId(id_cliente);

		if (cartoes.isEmpty()) {
			log.info("Cliente Id {} não possui cartões.", id_cliente);
			return;
		} else {
			cartoes.forEach(cartao -> {
				if (cartao instanceof CartaoCredito ccr) {
					ccr.setLimiteCredito(parametros.getLimiteCartaoCredito());
				} else if (cartao instanceof CartaoDebito cdb) {
					cdb.setLimiteDiario(parametros.getLimiteDiarioDebito());
				}
			});
			cartaoRepository.saveAll(cartoes);
		}
	}
	public void atualizarTaxasDosSeguros(Long id_cliente, PoliticaDeTaxas parametros) {
		List<SeguroBase> seguros = seguroRepository.findByClienteId(id_cliente);

		if (seguros.isEmpty()) {
			log.info("Cliente Id {} não possui seguros.", id_cliente);
			return;
		} else {
			seguros.forEach(seguro -> {
				if (seguro instanceof SeguroFraude sf) {
					sf.setPremioApolice(parametros.getTarifaSeguroFraude());
				} else if (seguro instanceof SeguroViagem sv) {
					sv.setPremioApolice(parametros.getTarifaSeguroViagem());
				}
			});		
			seguroRepository.saveAll(seguros);	
		}
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

}
