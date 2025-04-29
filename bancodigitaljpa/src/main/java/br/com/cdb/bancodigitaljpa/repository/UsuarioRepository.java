package br.com.cdb.bancodigitaljpa.repository;

import br.com.cdb.bancodigitaljpa.config.ConexaoPGadmin;
import br.com.cdb.bancodigitaljpa.enums.Role;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.model.Usuario;

import java.sql.*;

@Repository
public class UsuarioRepository {

	private final JdbcTemplate jdbcTemplate;

	public UsuarioRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// CREATE | Criar tabela
	public void criarTabela() {
		String sql = "CREATE TABLE IF NOT EXISTS usuario ("
				+ "id BIGSERIAL PRIMARY KEY, "
				+ "email VARCHAR(100) NOT NULL UNIQUE, "
				+ "senha VARCHAR(255) NOT NULL, "
				+ "role VARCHAR(20) NOT NULL);";

		jdbcTemplate.execute(sql);
	}

	// READ | Listar usuários
	public Usuario buscarUsuarioPorEmail(String email) {
		String sql = "SELECT * FROM usuario WHERE email = ?";

		return jdbcTemplate.query(
				sql,
				ps -> ps.setString(1, email), // PreparedStatementSetter
				rs -> {
					if (rs.next()) {
						return new Usuario(
								rs.getLong("id"),
								rs.getString("email"),
								rs.getString("senha"),
								Role.fromString(rs.getString("role"))
						);
					} else {
						throw new ResourceNotFoundException("Usuário não encontrado com email: " + email);
					}
				}
		);
	}

	// UPDATE | Inserir usuários
	public Usuario inserirUsuario(String email, String senha, Role role) {
		String sql = "INSERT INTO usuario (email, senha, role) VALUES (?, ?, ?) RETURNING id";

		return jdbcTemplate.query(
				sql,
				ps -> {
					ps.setString(1, email);
					ps.setString(2, senha);
					ps.setString(3, role.name());
				},
				rs -> {
					if (rs.next()) {
						long id = rs.getLong("id");
						return new Usuario(id, email, senha, role);
					} else {
						throw new RuntimeException("Erro ao inserir usuário e recuperar o ID.");
					}
				}
		);
	}

	// DELETE | Excluir usuários
	public void deletarUsuarioPorId(Long id) {
		String sql = "DELETE FROM usuario WHERE id = ?";
		int linhasAfetadas = jdbcTemplate.update(sql, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Usuário com ID " + id + " não encontrado para exclusão.");
		}
	}

}
