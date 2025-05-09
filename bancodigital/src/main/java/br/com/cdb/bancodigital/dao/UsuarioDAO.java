package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.mapper.UsuarioMapper;
import br.com.cdb.bancodigital.model.enums.Role;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.cdb.bancodigital.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioDAO {

	private final JdbcTemplate jdbcTemplate;
	private final UsuarioMapper usuarioMapper;

	// CREATE | Criar usuário
	public Usuario criarUsuario(String email, String senha, Role role) {
		log.info(ConstantUtils.INICIO_CRIAR_USUARIO_BANCO_DADOS);
		try {
			Long id = jdbcTemplate.queryForObject(
					SqlQueries.SQL_CREATE_USUARIO,
					Long.class,
					email,
					senha,
					role.name()
			);
			log.info(ConstantUtils.SUCESSO_CRIAR_USUARIO_BANCO_DADOS);
			return new Usuario(id, email, senha, role);
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_CRIAR_USUARIO_BANCO_DADOS, e);
			throw new SystemException(ConstantUtils.ERRO_CRIAR_USUARIO_BANCO_DADOS);
		}
	}

	// READ | Listar usuários
	public Optional<Usuario> buscarUsuarioPorEmail(String email) {
		log.info(ConstantUtils.INICIO_BUSCA_USUARIO);
		try {
			Usuario usuario = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_USUARIO_BY_EMAIL, usuarioMapper, email);
			if (usuario == null) {
				log.warn(ConstantUtils.ERRO_USUARIO_NULO);
				return Optional.empty();
			}
			log.info(ConstantUtils.USUARIO_ENCONTRADO);
			return Optional.of(usuario);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_USUARIO, ConstantUtils.RETORNO_VAZIO);
			return Optional.empty();
		}
	}
	public Usuario buscarUsuarioPorEmailOuErro(String email) {
		log.info(ConstantUtils.INICIO_BUSCA_USUARIO);
		return buscarUsuarioPorEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_USUARIO));
	}

	// Encontrar usuário pelo ID
	public Optional<Usuario> buscarUsuarioporId(Long id) {
		log.info(ConstantUtils.INICIO_BUSCA_USUARIO);
		try {
			Usuario usuario = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_USUARIO_BY_ID, usuarioMapper, id);
			if (usuario == null) {
				log.warn(ConstantUtils.ERRO_USUARIO_NULO);
				return Optional.empty();
			}
			log.info(ConstantUtils.USUARIO_ENCONTRADO);
            return Optional.of(usuario);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_USUARIO, ConstantUtils.RETORNO_VAZIO);
			return Optional.empty();
		}
	}

	// UPDATE | Atualizar usuários
	public void atualizarUsuario(Long id, String email, String senha, Role role) {
		log.info(ConstantUtils.INICIO_UPDATE_USUARIO, id);
		try {
			int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_UPDATE_USUARIO, email, senha, role.name(), id);
			if (linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_UPDATE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_USUARIO);
			}
			log.info(ConstantUtils.SUCESSO_UPDATE_USUARIO, id);
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_UPDATE_USUARIO, id, e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_UPDATE_USUARIO + id);
		}
	}

	// DELETE | Excluir usuários
	public void deletarUsuarioPorId(Long id) {
		log.info(ConstantUtils.INICIO_DELETE_USUARIO, id);
		try {
			int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_USUARIO, id);
			if (linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_DELETE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_USUARIO);
			}
			log.info(ConstantUtils.SUCESSO_DELETE_USUARIO, id);
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_DELETE_USUARIO, id, e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_DELETE_USUARIO + id);
		}

	}

}
