package br.com.cdb.bancodigitaljpa.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	// Cadastrar cliente
	public ClienteResponse addCliente(Cliente cliente) {
		validarCpfUnico(cliente.getCpf());
		validarMaiorIdade(cliente);

		clienteRepository.save(cliente);
		return toResponse(cliente);
	}

	// Ver cliente(s)
	public List<ClienteResponse> getClientes() {
		List<Cliente> clientes = clienteRepository.findAll();
		return clientes.stream().map(this::toResponse).toList();
	}

	public ClienteResponse getClienteById(Long id_cliente) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
		return toResponse(cliente);
	}

	// Excluir cadastro de cliente
	@Transactional
	public void deleteCliente(Long id_cliente) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));

		boolean temContas = !contaRepository.findByClienteId(id_cliente).isEmpty();
		boolean temCartoes = !cartaoRepository.findByContaClienteId(id_cliente).isEmpty();
		boolean temSeguros = !seguroRepository.findByClienteId(id_cliente).isEmpty();

		if (temContas || temCartoes || temSeguros) throw new ValidationException(
					"Cliente possui vínculos com contas, cartões ou seguros e não pode ser deletado.");

		clienteRepository.delete(cliente);
		log.info("Cliente ID {} deletado com sucesso", id_cliente);
	}

	// Atualizações de cliente
	@Transactional
	public ClienteResponse updateParcial(Long id_cliente, Map<String, Object> camposAtualizados) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));

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
			case "categoria":
				if (valor != null) {
					cliente.setCategoria(CategoriaCliente.valueOf(((String) valor).toUpperCase()));
				}
				break;
			}
		});
		
		clienteRepository.save(cliente);
		return toResponse(cliente);
	}

	@Transactional
	public ClienteResponse updateCliente(Long id_cliente, Cliente clienteAtualizado) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));

		// atualiza todos os campos
		cliente.setNome(clienteAtualizado.getNome());
		cliente.setCpf(clienteAtualizado.getCpf());
		cliente.setDataNascimento(clienteAtualizado.getDataNascimento());
		cliente.setEndereco(clienteAtualizado.getEndereco());
		cliente.setCategoria(clienteAtualizado.getCategoria());
		
		clienteRepository.save(cliente);
		return toResponse(cliente);
	}

	@Transactional
	public ClienteResponse updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));

		if (cliente.getCategoria().equals(novaCategoria))
			throw new InvalidInputParameterException("Cliente ID " + id_cliente + " já está na categoria "
					+ novaCategoria + ". Nenhuma atualização necessária.");

		cliente.setCategoria(novaCategoria);

		atualizarTaxasDasContasECartoesESeguros(id_cliente, novaCategoria);

		return toResponse(cliente);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void atualizarTaxasDasContasECartoesESeguros(Long id_cliente, CategoriaCliente novaCategoria) {
		try {
			PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(novaCategoria)
					.orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + novaCategoria));

			List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);

			if (contas.isEmpty())
				throw new InvalidInputParameterException("Cliente ID " + id_cliente + " não possui contas.");
			contas.forEach(conta -> {
				if (conta instanceof ContaCorrente cc) {
					cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
				} else if (conta instanceof ContaPoupanca cp) {
					cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
				}
			});

			contaRepository.saveAll(contas);

			List<CartaoBase> cartoes = cartaoRepository.findByContaClienteId(id_cliente);

			if (cartoes.isEmpty())
				throw new InvalidInputParameterException("Cliente ID " + id_cliente + " não possui cartões.");

			cartoes.forEach(cartao -> {
				if (cartao instanceof CartaoCredito ccr) {
					ccr.setLimiteCredito(parametros.getLimiteCartaoCredito());
				} else if (cartao instanceof CartaoDebito cdb) {
					cdb.setLimiteDiario(parametros.getLimiteDiarioDebito());
				}
			});
			
			cartaoRepository.saveAll(cartoes);

			List<SeguroBase> seguros = seguroRepository.findByClienteId(id_cliente);

			if (seguros.isEmpty())
				throw new InvalidInputParameterException("Cliente ID " + id_cliente + " não possui seguros.");

			seguros.forEach(seguro -> {
				if (seguro instanceof SeguroFraude sf) {
					sf.setPremioApolice(parametros.getTarifaSeguroFraude());
				} else if (seguro instanceof SeguroViagem sv) {
					sv.setPremioApolice(parametros.getTarifaSeguroViagem());
				}
			});
			
			seguroRepository.saveAll(seguros);

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

}
