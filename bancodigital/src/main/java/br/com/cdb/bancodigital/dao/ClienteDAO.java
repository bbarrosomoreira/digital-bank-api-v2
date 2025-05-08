package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.CommunicationException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.ClienteMapper;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteDAO {

	private final JdbcTemplate jdbcTemplate;
	private final ClienteMapper clienteMapper;

	// SAVE | Criar ou atualizar cliente
	public Cliente salvar(Cliente cliente) {
		if (cliente.getId() == null) {
			// Se não tiver ID, é um novo cliente (INSERT)
			return criarCliente(cliente);
		} else {
			// Se tiver ID, é um cliente existente (UPDATE)
			return atualizarCliente(cliente);
		}
	}

	// CREATE | Criar cliente
	public Cliente criarCliente(Cliente cliente) {
		Long id = jdbcTemplate.queryForObject(
				SqlQueries.SQL_CREATE_CLIENTE,
				Long.class,
				cliente.getNome(),
				cliente.getCpf(),
				cliente.getCategoria().name(),
				cliente.getDataNascimento(),
				cliente.getUsuario().getId()
		);

		cliente.setId(id);
		return cliente;
	}

	// READ | Listar clientes
	public List<Cliente> buscarTodosClientes() {
		return jdbcTemplate.query(SqlQueries.SQL_READ_ALL_CLIENTES, clienteMapper);
	}
	public Optional<Cliente> buscarClienteporId(Long id) {
		try {
			Cliente cliente = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CLIENTE_BY_ID, clienteMapper, id);
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public Optional<Cliente> buscarClienteporUsuario(Usuario usuario) {
		try {
			Cliente cliente = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CLIENTE_BY_USUARIO, clienteMapper, usuario.getId());
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	// Verificar se existe cliente com o CPF
	public boolean existsByCpf(String cpf) {
		// O resultado será o número de registros encontrados (1 ou mais)
		try {
			Integer count = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_CLIENTE, Integer.class, cpf);
			return count != null && count > 0;
		} catch (EmptyResultDataAccessException e) {
			return false;  // Retorna false se não houver registros
		} catch (DataAccessException e) {
			throw new CommunicationException("Erro ao acessar o banco de dados: " + e.getMessage());
		}
	}

	// UPDATE | Atualizar clientes
	private Cliente atualizarCliente(Cliente cliente) {
		int linhasAfetadas = jdbcTemplate.update(
				SqlQueries.SQL_UPDATE_CLIENTE,
				cliente.getNome(),
				cliente.getCpf(),
				cliente.getCategoria().name(),
				cliente.getDataNascimento(),
				cliente.getId()
		);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Cliente não encontrado para atualização com ID: " + cliente.getId());
		}

		return cliente;
	}

	// DELETE | Excluir clientes
	public void deletarClientePorId(Long id) {
		int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_CLIENTE, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Cliente com ID " + id + " não encontrado para exclusão.");
		}
	}

}
