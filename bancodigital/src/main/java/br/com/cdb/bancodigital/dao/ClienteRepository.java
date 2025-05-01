package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.model.Cliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepository {

	private final JdbcTemplate jdbcTemplate;

	public ClienteRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// SAVE | Criar ou atualizar cliente
	public Cliente save(Cliente cliente) {
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
		String sql = ""
	}

	// READ | Listar clientes

	// UPDATE | Atualizar clientes
	private Cliente atualizarCliente(Cliente cliente) {
		String sql = "UPDATE cliente SET nome = ?, email = ?, telefone = ?, categoria = ?, cpf = ?, usuario_id = ? " +
				"WHERE id = ?";

		int linhasAfetadas = jdbcTemplate.update(
				sql,
				cliente.getNome(),
				cliente.getEmail(),
				cliente.getTelefone(),
				cliente.getCategoria().name(),
				cliente.getCpf(),
				cliente.getUsuario().getId(),
				cliente.getId()
		);

		if (linhasAfetadas == 0) {
			throw new RuntimeException("Cliente não encontrado para atualização.");
		}
		return cliente;
	}

	// DELETE | Excluir clientes


//	boolean existsByCpf(String cpf);
//	Optional<Cliente> findByUsuario(Usuario usuario);
//	Optional<Cliente> findByUsuarioId(Long id);
}
