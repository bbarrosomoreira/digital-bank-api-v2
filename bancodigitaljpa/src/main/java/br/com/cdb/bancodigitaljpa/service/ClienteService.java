package br.com.cdb.bancodigitaljpa.service;

import java.math.BigDecimal;
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

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.exceptions.ClienteNaoEncontradoException;
import br.com.cdb.bancodigitaljpa.repository.ClienteRepository;
import br.com.cdb.bancodigitaljpa.repository.ContaRepository;

@Service
public class ClienteService {

	private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired ContaRepository contaRepository;

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
					if(valor != null) {
						cliente.setNome((String) valor);
					}
					break;
				case "cpf":
					if(valor != null) {
						//validar cpf
						String novoCpf = (String) valor;
						cliente.setCpf(novoCpf);
					}
					break;
				case "dataNascimento":
					if(valor != null) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
						cliente.setDataNascimento(LocalDate.parse((String) valor, formatter));
					}
					break;
				case "categoria":
					if(valor != null) {
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

		//atualiza todos os campos
		clienteExistente.setNome(clienteAtualizado.getNome());
		clienteExistente.setCpf(clienteAtualizado.getCpf());
		clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
		clienteExistente.setCategoria(clienteAtualizado.getCategoria());
		
		return clienteRepository.save(clienteExistente);
	}
	
	@Transactional
	public Cliente updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) {
		Cliente cliente = clienteRepository.findById(id_cliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id_cliente));

		cliente.setCategoria(novaCategoria);
		clienteRepository.save(cliente);

		atualizarTaxasDasContas(id_cliente, cliente.getCategoria());

		return cliente;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void atualizarTaxasDasContas(Long id_cliente, CategoriaCliente novaCategoria) {
		try {
			List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);

			if (contas.isEmpty()) {
				log.warn("Cliente ID {} não possui contas.", id_cliente);
				return;
			}

			contas.forEach(conta -> {
				if (conta instanceof ContaCorrente cc) {
					cc.setTaxaManutencao(calcularTaxaManutencao(novaCategoria));
				} else if (conta instanceof ContaPoupanca cp) {
					cp.setTaxaRendimento(calcularTaxaRendimento(novaCategoria));
				}
			});

			contaRepository.saveAll(contas);
		} catch (Exception e) {
			log.error("Falha ao atualizar taxas das contas do cliente ID {}", id_cliente, e);
			throw e;
		}
	}
	
	public BigDecimal calcularTaxaManutencao(CategoriaCliente categoria) {
		return switch (categoria) {
		case COMUM -> TAXA_COMUM_MANUTENCAO;
		case SUPER -> TAXA_SUPER_MANUTENCAO;
		case PREMIUM -> TAXA_PREMIUM_MANUTENCAO;
		};
	}
	public BigDecimal calcularTaxaRendimento(CategoriaCliente categoria) {
		return switch (categoria) {
		case COMUM -> TAXA_COMUM_RENDIMENTO;
		case SUPER -> TAXA_SUPER_RENDIMENTO;
		case PREMIUM -> TAXA_PREMIUM_RENDIMENTO;
		};
	}
	
	private static final BigDecimal TAXA_COMUM_MANUTENCAO = new BigDecimal("12.00");
	private static final BigDecimal TAXA_SUPER_MANUTENCAO = new BigDecimal("8.00");
	private static final BigDecimal TAXA_PREMIUM_MANUTENCAO = BigDecimal.ZERO;
	
	private static final BigDecimal TAXA_COMUM_RENDIMENTO = new BigDecimal("0.005");
	private static final BigDecimal TAXA_SUPER_RENDIMENTO = new BigDecimal("0.007");
	private static final BigDecimal TAXA_PREMIUM_RENDIMENTO = new BigDecimal("0.009");
	
}
