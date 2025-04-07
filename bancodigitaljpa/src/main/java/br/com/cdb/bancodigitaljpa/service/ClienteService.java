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

import br.com.cdb.bancodigitaljpa.dto.ClienteResponse;
import br.com.cdb.bancodigitaljpa.entity.CartaoBase;
import br.com.cdb.bancodigitaljpa.entity.CartaoCredito;
import br.com.cdb.bancodigitaljpa.entity.CartaoDebito;
import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
import br.com.cdb.bancodigitaljpa.entity.SeguroFraude;
import br.com.cdb.bancodigitaljpa.entity.SeguroViagem;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.exceptions.ResourceAlreadyExistsException;
import br.com.cdb.bancodigitaljpa.exceptions.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
import br.com.cdb.bancodigitaljpa.repository.ClienteRepository;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigitaljpa.repository.SeguroRepository;
import jakarta.validation.ValidationException;

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
				.orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id_cliente + " não encontrado."));
		return toResponse(cliente);
	}

	// Excluir cadastro de cliente
	@Transactional
	public void deleteCliente(Long id_cliente) {
		Cliente cliente = clienteRepository.findById(id_cliente)
			.orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id_cliente + " não encontrado."));

		boolean temContas = !contaRepository.findByClienteId(id_cliente).isEmpty();
		boolean temCartoes = !cartaoRepository.findByContaClienteId(id_cliente).isEmpty();
		boolean temSeguros = !seguroRepository.findByClienteId(id_cliente).isEmpty();

		if (temContas || temCartoes || temSeguros) {
			throw new ValidationException("Cliente possui vínculos com contas, cartões ou seguros e não pode ser deletado.");
		}

		clienteRepository.delete(cliente);
		log.info("Cliente ID {} deletado com sucesso", id_cliente);
	}

	// Atualizações de cliente
	@Transactional
	public ClienteResponse updateParcial(Long id_cliente, Map<String, Object> camposAtualizados) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id_cliente + " não encontrado."));

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
					// validar cpf
					String novoCpf = (String) valor;
					cliente.setCpf(novoCpf);
				}
				break;
			case "dataNascimento":
				if (valor != null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					cliente.setDataNascimento(LocalDate.parse((String) valor, formatter));
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
				.orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id_cliente + " não encontrado."));

		// atualiza todos os campos
		cliente.setNome(clienteAtualizado.getNome());
		cliente.setCpf(clienteAtualizado.getCpf());
		cliente.setDataNascimento(clienteAtualizado.getDataNascimento());
		cliente.setCategoria(clienteAtualizado.getCategoria());
		clienteRepository.save(cliente);	
		return toResponse(cliente);
	}

	// CRIAR CLIENTE RESPONSE
	@Transactional
	public ClienteResponse updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id_cliente + " não encontrado."));

		if (cliente.getCategoria().equals(novaCategoria)) {
			log.info("Cliente ID {} já está na categoria {}. Nenhuma atualização necessária.", id_cliente,
					novaCategoria);
			return toResponse(cliente);
		}

		cliente.setCategoria(novaCategoria);
		clienteRepository.save(cliente);

		Cliente clienteAtualizado = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new IllegalStateException("Falha ao recuperar cliente atualizado"));

		if (clienteAtualizado.getCategoria() != novaCategoria) {
			throw new IllegalStateException("Erro: Categoria do cliente não foi atualizada no banco!");
		}

		atualizarTaxasDasContasECartoesESeguros(id_cliente, cliente.getCategoria());

		return toResponse(clienteAtualizado);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void atualizarTaxasDasContasECartoesESeguros(Long id_cliente, CategoriaCliente novaCategoria) {
		try {
			PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(novaCategoria).orElseThrow(
					() -> new RuntimeException("Parâmetros não encontrados para a categoria: " + novaCategoria));

//			conta.setarTarifa(null);

			List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);

			if (contas.isEmpty()) {
				log.warn("Cliente ID {} não possui contas.", id_cliente);
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

			List<CartaoBase> cartoes = cartaoRepository.findByContaClienteId(id_cliente);

			if (cartoes.isEmpty()) {
				log.warn("Cliente Id {} não possui cartões.", id_cliente);
			} else {
				cartoes.forEach(cartao -> {
					if (cartao instanceof CartaoCredito ccr) {
						ccr.setLimiteCredito(parametros.getLimiteCartaoCredito());
					} else if (cartao instanceof CartaoDebito cdb) {
						cdb.setLimiteDiario(parametros.getLimiteDiarioDebito());
					}
				});
			}

			List<SeguroBase> seguros = seguroRepository.findByClienteId(id_cliente);

			if (seguros.isEmpty()) {
				log.warn("Cliente Id {} não possui seguros.", id_cliente);
			} else {
				seguros.forEach(seguro -> {
					if (seguro instanceof SeguroFraude sf) {
						sf.setPremioApolice(parametros.getTarifaSeguroFraude());
					} else if (seguro instanceof SeguroViagem sv) {
						sv.setPremioApolice(parametros.getTarifaSeguroViagem());
					}
				});
			}

		} catch (Exception e) {
			log.error("Falha ao atualizar taxas das contas do cliente ID {}", id_cliente, e);
			throw e;
		}
	}

	// M
	public ClienteResponse toResponse(Cliente cliente) {
		return new ClienteResponse(cliente);
	}
	private void validarCpfUnico(String cpf) {
		if (clienteRepository.existsByCpf(cpf))
			throw new ResourceAlreadyExistsException("Já existe um cadastro no sistema com o CPF informado!");
	}

	private void validarMaiorIdade(Cliente cliente) {
		if (!cliente.isMaiorDeIdade())
			throw new ValidationException("Cliente deve ser maior de idade para se cadastrar.");
	}

}
