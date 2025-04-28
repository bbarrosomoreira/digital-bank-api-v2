package br.com.cdb.bancodigitaljpa.repository;

import br.com.cdb.bancodigitaljpa.config.ConexaoPGadmin;
import br.com.cdb.bancodigitaljpa.enums.Role;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.model.Usuario;

import java.sql.*;

@Repository
public class UsuarioRepository {

	private final ConexaoPGadmin conexaoPGadmin;

	public UsuarioRepository(ConexaoPGadmin conexaoPGadmin) {
		this.conexaoPGadmin = conexaoPGadmin;
	}

	// CREATE | Criar tabela
	public void criarTabela() {
		String sql = "CREATE TABLE IF NOT EXISTS usuario ("
				+ "id BIGSERIAL PRIMARY KEY, "
				+ "email VARCHAR(100) NOT NULL UNIQUE, "
				+ "senha VARCHAR(255) NOT NULL, "
				+ "role VARCHAR(20) NOT NULL"
				+ ");";

		try (Connection conexao = conexaoPGadmin.getConnection();
			 Statement stmt = conexao.createStatement()) {
			stmt.execute(sql);
			System.out.println("Tabela usuario criada com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao criar tabela usuário no banco de dados: " + e.getMessage(), e);
		}
	}

	// READ | Listar usuários
	public Usuario buscarUsuarioPorEmail(String email) {
		String sql = "SELECT * FROM usuario WHERE email = ?";

		try (Connection conexao = conexaoPGadmin.getConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Usuario(
						rs.getLong("id"),
						rs.getString("email"),
						rs.getString("senha"),
						Role.fromString(rs.getString("role"))
				);
			} else {
				throw new ResourceNotFoundException("Erro ao encontrar o usuário através do email.");
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao buscar usuário no banco de dados: " + e.getMessage(), e);
		}

	}

	// UPDATE | Inserir usuários
	public void inserirUsuario(String email, String senha, Role role) {
		String sql = "INSERT INTO usuario (email, senha, role) VALUES (?, ?, ?)";

		try (Connection conexao = conexaoPGadmin.getConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setString(1, email);
			stmt.setString(2, senha);
			stmt.setString(3, role.name());

			stmt.executeUpdate();
			System.out.println("Usuario inserido com sucesso!");

		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir usuário no banco de dados: " + e.getMessage(), e);
		}
	}

	// DELETE | Exclui usuários

}
