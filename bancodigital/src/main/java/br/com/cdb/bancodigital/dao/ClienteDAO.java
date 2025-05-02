package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.ClienteMapper;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Usuario;
import lombok.RequiredArgsConstructor;
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
		String sql = "INSERT INTO cliente (nome, cpf, categoria, data_nascimento, usuario_id) VALUES (?, ?, ?, ?, ?) RETURNING id";

		Long id = jdbcTemplate.queryForObject(
				sql,
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
		String sql = "SELECT * FROM cliente";
		return jdbcTemplate.query(sql, clienteMapper);
	}
	public Optional<Cliente> buscarClienteporId(Long id) {
		String sql = "SELECT * FROM cliente WHERE id = ?";
		try {
			Cliente cliente = jdbcTemplate.queryForObject(sql, clienteMapper, id);
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public Optional<Cliente> buscarClienteporUsuario(Usuario usuario) {
		String sql = "SELECT * FROM cliente WHERE usuario_id = ?";
		try {
			Cliente cliente = jdbcTemplate.queryForObject(sql, clienteMapper, usuario.getId());
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public Optional<Cliente> buscarClienteporUsuarioId(Long id) {
		String sql = "SELECT * FROM cliente WHERE usuario_id = ?";
		try {
			Cliente cliente = jdbcTemplate.queryForObject(sql, clienteMapper, id);
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public Optional<Cliente> buscarClienteporCPF(String cpf) {
		String sql = "SELECT * FROM cliente WHERE cpf = ?";
		try {
			Cliente cliente = jdbcTemplate.queryForObject(sql, clienteMapper, cpf);
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	// Verificar se existe cliente com o CPF
	public boolean existsByCpf(String cpf) {
		String sql = "SELECT COUNT(*) FROM cliente WHERE cpf = ?";

		// O resultado será o número de registros encontrados (1 ou mais)
		int count = jdbcTemplate.queryForObject(sql, Integer.class, cpf);

		return count > 0;  // Retorna true se existir pelo menos um cliente com o CPF
	}

	// UPDATE | Atualizar clientes
	private Cliente atualizarCliente(Cliente cliente) {
		String sql = "UPDATE cliente SET nome = ?, cpf = ?, categoria = ?, data_nascimento = ?, usuario_id = ? WHERE id = ?";

		int linhasAfetadas = jdbcTemplate.update(
				sql,
				cliente.getNome(),
				cliente.getCpf(),
				cliente.getCategoria().name(),
				cliente.getDataNascimento(),
				cliente.getUsuario().getId(),
				cliente.getId()
		);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Cliente não encontrado para atualização com ID: " + cliente.getId());
		}

		return cliente;
	}

	// DELETE | Excluir clientes
	public void deletarClientePorId(Long id) {
		String sql = "DELETE FROM cliente WHERE id = ?";
		int linhasAfetadas = jdbcTemplate.update(sql, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Cliente com ID " + id + " não encontrado para exclusão.");
		}
	}

}
