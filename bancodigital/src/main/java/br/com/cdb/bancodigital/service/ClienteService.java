package br.com.cdb.bancodigital.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.dto.ClienteDTO;
import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.EnderecoCliente;
import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
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
import br.com.cdb.bancodigital.dto.response.ClienteResponse;

@Service
@RequiredArgsConstructor
public class ClienteService {

	private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

	private final ClienteDAO clienteDAO;
	private final ContaDAO contaDAO;
	private final CartaoDAO cartaoDAO;
	private final SeguroDAO seguroDAO;
	private final PoliticaDeTaxasDAO politicaDeTaxaDAO;
	private final ReceitaService receitaService;
	private final SecurityService securityService;
    private final BrasilApiService brasilApiService;

	// Cadastrar cliente
	public ClienteResponse cadastrarCliente(ClienteDTO dto, Usuario usuario) {
		CEP2 cepInfo = brasilApiService.buscarEnderecoPorCep(dto.getCep());
		
		Cliente cliente = dto.transformaParaObjeto();
		cliente.setCategoria(CategoriaCliente.COMUM);

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

		clienteDAO.salvar(cliente);
		return toResponse(cliente);
	}

	// Ver cliente(s)
	public List<ClienteResponse> getClientes() throws AccessDeniedException { //só admin
		List<Cliente> clientes = clienteDAO.buscarTodosClientes();
		return clientes.stream().map(this::toResponse).toList();
	} 

	public Cliente getClienteById(Long id_cliente, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		securityService.validateAccess(usuarioLogado, cliente);
		return cliente;
	}
	public ClienteResponse buscarClienteDoUsuario(Usuario usuario) {
	    Cliente cliente = clienteDAO.buscarClienteporUsuario(usuario)
	        .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o usuário logado."));
	    return toResponse(cliente);
	}

	// deletar cadastro de cliente
	@Transactional
	public void deleteCliente(Long id_cliente) throws AccessDeniedException { //só admin
		Cliente cliente = verificarClienteExistente(id_cliente);
		
		boolean temContas = contaDAO.existsByClienteId(id_cliente);
		boolean temCartoes = cartaoDAO.existsByContaClienteId(id_cliente);
		boolean temSeguros = seguroDAO.existsByCartaoCreditoContaClienteId(id_cliente);

		if (temContas || temCartoes || temSeguros) throw new ValidationException(
					"Cliente possui vínculos com contas, cartões ou seguros e não pode ser deletado.");

		clienteDAO.deletarClientePorId(cliente.getId());
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
		
		clienteDAO.salvar(cliente);
		return toResponse(cliente);
	}

	@Transactional
	public ClienteResponse updateCliente(Long id_cliente, ClienteDTO dto, Usuario usuarioLogado) {
		Cliente cliente = verificarClienteExistente(id_cliente);
		
		securityService.validateAccess(usuarioLogado, cliente);
		
		// atualiza todos os campos
		cliente = dto.transformaParaObjeto();
		if(!receitaService.isCpfValidoEAtivo(cliente.getCpf())) throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");		
		validarCpfUnico(cliente.getCpf());
		validarMaiorIdade(cliente);
		
		clienteDAO.salvar(cliente);
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
		Optional<Cliente> clienteExistente = clienteDAO.buscarClienteporCPF(cpf);
		if (clienteExistente.isPresent())
			throw new ResourceAlreadyExistsException("CPF já cadastrado no sistema.");
	}
	private void validarMaiorIdade(Cliente cliente) {
		if (!cliente.isMaiorDeIdade())
			throw new ValidationException("Cliente deve ser maior de 18 anos para se cadastrar.");
	}
	public void atualizarTaxasDasContas(Long id_cliente, PoliticaDeTaxas parametros) {
		List<Conta> contas = contaDAO.findByClienteId(id_cliente);

		if (contas.isEmpty()) {
			log.info("Cliente ID {} não possui contas.", id_cliente);
			return;
		} else {
			contas.forEach(conta -> {
				switch (conta.getTipoConta()) {
					case CORRENTE -> {
						conta.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
					}
					case POUPANCA -> {
						conta.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
					}
					case INTERNACIONAL -> {
						conta.setTarifaManutencao(parametros.getTarifaManutencaoContaInternacional());
					}
				}
			});
			contaDAO.saveAll(contas);		
		}
	}
	public void atualizarTaxasDosCartoes(Long id_cliente, PoliticaDeTaxas parametros) {
		List<Cartao> cartoes = cartaoDAO.findByContaClienteId(id_cliente);

		if (cartoes.isEmpty()) {
			log.info("Cliente Id {} não possui cartões.", id_cliente);
			return;
		} else {
			cartoes.forEach(cartao -> {
				switch (cartao.getTipoCartao()){
					case CREDITO -> {
						cartao.setLimite(parametros.getLimiteCartaoCredito());
					}
					case DEBITO -> {
						cartao.setLimite(parametros.getLimiteDiarioDebito());
					}
				}
			});
			cartaoDAO.saveAll(cartoes);
		}
	}
	public void atualizarTaxasDosSeguros(Long id_cliente, PoliticaDeTaxas parametros) {
		List<Seguro> seguros = seguroDAO.findByClienteId(id_cliente);

		if (seguros.isEmpty()) {
			log.info("Cliente Id {} não possui seguros.", id_cliente);
			return;
		} else {
			seguros.forEach(seguro -> {
				switch (seguro.getTipoSeguro()){
					case FRAUDE -> {
						seguro.setPremioApolice(parametros.getTarifaSeguroFraude());
					}
					case VIAGEM -> {
						seguro.setPremioApolice(parametros.getTarifaSeguroViagem());
					}
				}

			});		
			seguroDAO.saveAll(seguros);	
		}
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

}
