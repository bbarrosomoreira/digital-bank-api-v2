package br.com.cdb.bancodigitaljpa.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import br.com.cdb.bancodigitaljpa.config.ConexaoPGadmin;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.model.Cliente;
import br.com.cdb.bancodigitaljpa.model.Usuario;

@Repository
public class ClienteRepository {

	private final ConexaoPGadmin conexaoPGadmin;

	public ClienteRepository(ConexaoPGadmin conexaoPGadmin) {
		this.conexaoPGadmin = conexaoPGadmin;
	}

	// Criar tabela
	public void criarTabela() {
		String sql = "CREATE TABLE IF NOT EXISTS cliente ("
				+ "id BIGSERIAL PRIMARY KEY, "
				+ "nome VARCHAR(100) NOT NULL, "
				+ "cpf VARCHAR(11) NOT NULL UNIQUE, "
				+ "categoria VARCHAR(20) NOT NULL, "
				+ "data_nascimento DATE NOT NULL, "
				+ "usuario_id BIGINT NOT NULL UNIQUE, "
				+ "endereco_cep VARCHAR(10), "
				+ "endereco_rua VARCHAR(255), "
				+ "endereco_numero INT, "
				+ "endereco_complemento VARCHAR(10), "
				+ "endereco_bairro VARCHAR(100), "
				+ "endereco_cidade VARCHAR(100), "
				+ "endereco_estado VARCHAR(2), "
				+ "FOREIGN KEY (usuario_id) REFERENCES usuario(id)"
				+ ");";

		try (Connection conexao = conexaoPGadmin.getConnection();
			 Statement stmt = conexao.createStatement()) {
			stmt.execute(sql);
			System.out.println("Tabela cliente criada com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao criar tabela cliente no banco de dados: " + e.getMessage(), e);
		}
	}

//	boolean existsByCpf(String cpf);
//	Optional<Cliente> findByUsuario(Usuario usuario);
//	Optional<Cliente> findByUsuarioId(Long id);
}
