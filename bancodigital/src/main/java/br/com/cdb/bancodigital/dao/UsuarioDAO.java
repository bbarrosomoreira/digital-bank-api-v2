package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.mapper.UsuarioMapper;
import br.com.cdb.bancodigital.model.enums.Role;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.cdb.bancodigital.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioDAO {

	private final JdbcTemplate jdbcTemplate;
	private final UsuarioMapper usuarioMapper;

	// CREATE | Criar usuário
	public Usuario criarUsuario(String email, String senha, Role role) {
		Long id = jdbcTemplate.queryForObject(
				SqlQueries.SQL_CREATE_USUARIO,
				Long.class,
				email,
				senha,
				role.name()
		);

		return new Usuario(id, email, senha, role);
	}

	// READ | Listar usuários
	public Optional<Usuario> buscarUsuarioPorEmail(String email) {
		try {
			Usuario usuario = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_USUARIO_BY_EMAIL, usuarioMapper, email);
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
		try {
			Usuario usuario = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_USUARIO_BY_ID, usuarioMapper, id);
			return Optional.of(usuario);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	// UPDATE | Atualizar usuários
	public void atualizarUsuario(Long id, String email, String senha, Role role) {
		int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_UPDATE_USUARIO, email, senha, role.name(), id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Usuário com ID " + id + " não encontrado para atualização.");
		}
	}

	// DELETE | Excluir usuários
	public void deletarUsuarioPorId(Long id) {
		int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_USUARIO, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Usuário com ID " + id + " não encontrado para exclusão.");
		}
	}

}
