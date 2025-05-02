package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.mapper.UsuarioMapper;
import br.com.cdb.bancodigital.model.enums.Role;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.cdb.bancodigital.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioDAO {

	private final JdbcTemplate jdbcTemplate;
	private final UsuarioMapper usuarioMapper;

	// CREATE | Criar usuário
	public Usuario criarUsuario(String email, String senha, Role role) {
		String sql = "INSERT INTO usuario (email, senha, role) VALUES (?, ?, ?) RETURNING id";

		Long id = jdbcTemplate.queryForObject(
				sql,
				Long.class,
				email,
				senha,
				role.name()
		);

		return new Usuario(id, email, senha, role);
	}

	// READ | Listar usuários
	public Optional<Usuario> buscarUsuarioPorEmail(String email) {
		String sql = "SELECT * FROM usuario WHERE email = ?";
		try {
			Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioMapper, email);
			return Optional.of(usuario);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public Usuario buscarUsuarioPorEmailOuErro(String email) {
		return buscarUsuarioPorEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com e-mail: " + email));
	}

	// Encontrar usuário pelo ID
	public Optional<Usuario> buscarUsuarioporId(Long id) {
		String sql = "SELECT * FROM usuario WHERE id = ?";
		try {
			Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioMapper, id);
			return Optional.of(usuario);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	// UPDATE | Atualizar usuários
	public void atualizarUsuario(Long id, String email, String senha, Role role) {
		String sql = "UPDATE usuario SET email = ?, senha = ?, role = ? WHERE id = ?";

		int linhasAfetadas = jdbcTemplate.update(sql, email, senha, role.name(), id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Usuário com ID " + id + " não encontrado para atualização.");
		}
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
