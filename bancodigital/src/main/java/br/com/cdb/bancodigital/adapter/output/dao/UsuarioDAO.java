package br.com.cdb.bancodigital.adapter.output.dao;

import br.com.cdb.bancodigital.application.port.out.repository.UsuarioRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.application.core.domain.mapper.UsuarioMapper;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Role;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioDAO implements UsuarioRepository {

	private final JdbcTemplate jdbcTemplate;
	private final UsuarioMapper usuarioMapper;

	// CREATE | Criar usuário
	public Usuario add(String email, String senha, Role role) {
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
	public boolean existWithEmail(String email) {
		log.info(ConstantUtils.INICIO_BUSCA_USUARIO);
		try {
			Boolean existe = jdbcTemplate.queryForObject(
					SqlQueries.SQL_EXIST_USUARIO_BY_EMAIL,
					Boolean.class,
					email);

			log.info(ConstantUtils.USUARIO_ENCONTRADO);
			return Boolean.TRUE.equals(existe);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_USUARIO, ConstantUtils.RETORNO_VAZIO);
			return false;
		}
	}
	public Usuario findByEmail(String email) {
		log.info(ConstantUtils.INICIO_BUSCA_USUARIO);
		try {
			Usuario usuario = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_USUARIO_BY_EMAIL, usuarioMapper, email);
			if (usuario == null) {
				log.warn(ConstantUtils.ERRO_USUARIO_NULO);
				return null;
			}
			log.info(ConstantUtils.USUARIO_ENCONTRADO);
			return usuario;
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_USUARIO, ConstantUtils.RETORNO_VAZIO);
			return null;
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_BUSCA_USUARIO, email, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_USUARIO + email);
		}
	}

	// Encontrar usuário pelo ID
	public Optional<Usuario> findById(Long id) {
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
	public void update(Long id, String email, String senha, Role role) {
		log.info(ConstantUtils.INICIO_UPDATE_USUARIO, id);
		try {
			Integer linhasAfetadas = jdbcTemplate.queryForObject(
					SqlQueries.SQL_UPDATE_USUARIO,
					Integer.class,
					id,
					email,
					senha,
					role.name()
					);
			if (linhasAfetadas == null || linhasAfetadas == 0) {
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
	public void delete(Long id) {
		log.info(ConstantUtils.INICIO_DELETE_USUARIO, id);
		try {
			Integer linhasAfetadas = jdbcTemplate.queryForObject(
					SqlQueries.SQL_DELETE_USUARIO,
					Integer.class,
					id);
			if (linhasAfetadas == null || linhasAfetadas == 0) {
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
