package br.com.cdb.bancodigitaljpa.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.exceptions.ClienteNaoEncontradoException;
import br.com.cdb.bancodigitaljpa.repository.CartaoRepository;
import br.com.cdb.bancodigitaljpa.repository.ClienteRepository;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;
import br.com.cdb.bancodigitaljpa.repository.PoliticaDeTaxasRepository;

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
	private PoliticaDeTaxasRepository politicaDeTaxaRepository;

	// Cadastrar cliente
	public Cliente addCliente(Cliente cliente) {

		return clienteRepository.save(cliente);
	}

	// Ver cliente(s)
	public List<Cliente> getClientes() {
		return clienteRepository.findAll();
	}

	public Cliente getClienteById(Long id_cliente) {
		return clienteRepository.findById(id_cliente).orElseThrow(() -> new ClienteNaoEncontradoException(id_cliente));
	}

	// Excluir cadastro de cliente
	@Transactional
	public boolean deleteCliente(Long id_cliente) {
		try {
			clienteRepository.deleteById(id_cliente);
			log.debug("Cliente ID {} deletado com sucesso", id_cliente);
			return true;
		} catch (EmptyResultDataAccessException e) {
			log.warn("Tentativa de deletar cliente ID {} inexistente", id_cliente);
			return false;
		} catch (Exception e) {
			log.error("Falha ao deletar cliente com ID {} ", id_cliente, e);
			return false;
		}
	}

	// Atualizações de cliente
	@Transactional
	public Cliente updateParcial(Long id_cliente, Map<String, Object> camposAtualizados) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id_cliente));

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

		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente updateCliente(Long id_cliente, Cliente clienteAtualizado) {
		Cliente clienteExistente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id_cliente));

		// atualiza todos os campos
		clienteExistente.setNome(clienteAtualizado.getNome());
		clienteExistente.setCpf(clienteAtualizado.getCpf());
		clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
		clienteExistente.setCategoria(clienteAtualizado.getCategoria());

		return clienteRepository.save(clienteExistente);
	}

	// CRIAR CLIENTE RESPONSE
	@Transactional
	public Cliente updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id_cliente));

		if (cliente.getCategoria().equals(novaCategoria)) {
			log.info("Cliente ID {} já está na categoria {}. Nenhuma atualização necessária.", id_cliente,
					novaCategoria);
			return cliente;
		}
		
		cliente.setCategoria(novaCategoria);
		clienteRepository.save(cliente);

		Cliente clienteAtualizado = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new IllegalStateException("Falha ao recuperar cliente atualizado"));

		if (clienteAtualizado.getCategoria() != novaCategoria) {
			throw new IllegalStateException("Erro: Categoria do cliente não foi atualizada no banco!");
		}

		atualizarTaxasDasContasECartoes(id_cliente, cliente.getCategoria());

		return clienteAtualizado;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void atualizarTaxasDasContasECartoes(Long id_cliente, CategoriaCliente novaCategoria) {
		try {
			PoliticaDeTaxas parametros = politicaDeTaxaRepository.findByCategoria(novaCategoria).orElseThrow(
					() -> new RuntimeException("Parâmetros não encontrados para a categoria: " + novaCategoria));

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

		} catch (Exception e) {
			log.error("Falha ao atualizar taxas das contas do cliente ID {}", id_cliente, e);
			throw e;
		}
	}

}
