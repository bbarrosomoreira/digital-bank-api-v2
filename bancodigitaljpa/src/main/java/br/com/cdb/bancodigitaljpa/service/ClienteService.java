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

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.exceptions.ClienteNaoEncontradoException;
import br.com.cdb.bancodigitaljpa.repository.ClienteRepository;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

	private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente addCliente(String nome, String cpf, LocalDate dataNascimento) {

		Cliente cliente = new Cliente();
		cliente.setCpf(cpf);
		cliente.setNome(nome);
		cliente.setDataNascimento(dataNascimento);

		// VALIDAR SE CPF JA ESTA CADASTRADO
//		if (clienteRepository.exists(cliente.getCpf())) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado");
//		}

		return clienteRepository.save(cliente);
	}

	public List<Cliente> getClientes() {
		return clienteRepository.findAll();
	}

	public Cliente getClienteById(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> new ClienteNaoEncontradoException(id));
	}

	@Transactional
	public boolean deleteCliente(Long id) {

		try {
			clienteRepository.deleteById(id);
			log.debug("Cliente ID {} deletado com sucesso", id);
			return true;
		} catch (EmptyResultDataAccessException e) {
			log.warn("Tentativa de deletar cliente ID {} inexistente", id);
			return false;
		} catch (Exception e) {
			log.error("Falha ao deletar cliente com ID {} ", id, e);
			return false;
		}
	}

	@Transactional
	public Cliente updateParcial(Long id, Map<String, Object> camposAtualizados) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id));

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
	public Cliente updateCliente(Long id, Cliente clienteAtualizado) {
		Cliente clienteExistente = clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNaoEncontradoException(id));

		//atualiza todos os campos
		clienteExistente.setNome(clienteAtualizado.getNome());
		clienteExistente.setCpf(clienteAtualizado.getCpf());
		//ADD: validar cpf
		clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
		clienteExistente.setCategoria(clienteAtualizado.getCategoria());
		
		return clienteRepository.save(clienteExistente);
	}
}
