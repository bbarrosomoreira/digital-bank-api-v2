package br.com.cdb.bancodigitaljpa.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepository {

	private final JdbcTemplate jdbcTemplate;

	public ClienteRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// CREATE | Criar cliente


	// READ | Listar clientes

	// UPDATE | Atualizar clientes

	// DELETE | Excluir clientes


//	boolean existsByCpf(String cpf);
//	Optional<Cliente> findByUsuario(Usuario usuario);
//	Optional<Cliente> findByUsuarioId(Long id);
}
