package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.mapper.SeguroMapper;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeguroDAO {

	private final JdbcTemplate jdbcTemplate;
	private final SeguroMapper seguroMapper;

	// SAVE | Criar ou atualizar cartão
	public Seguro salvar(Seguro seguro) {
		log.info(ConstantUtils.INICIO_SALVAR_SEGURO);
		try {
			if (seguro.getId() == null) {
				// Se não tiver ID, é um novo seguro (INSERT)
				return criarSeguro(seguro);
			} else {
				// Se tiver ID, é um seguro existente (UPDATE)
				return atualizarSeguro(seguro);
			}
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_SALVAR_SEGURO, e);
			throw new SystemException(ConstantUtils.ERRO_SALVAR_SEGURO);
		}
	}

	// CREATE
	public Seguro criarSeguro(Seguro seguro) {
		log.info(ConstantUtils.INICIO_CRIAR_SEGURO_BANCO_DADOS);
		try {
			Long id = jdbcTemplate.queryForObject(SqlQueries.SQL_CREATE_SEGURO, Long.class,
					seguro.getTipoSeguro().name(),
					seguro.getNumApolice(),
					seguro.getCartao().getId(),
					seguro.getDataContratacao(),
					seguro.getValorApolice(),
					seguro.getDescricaoCondicoes(),
					seguro.getPremioApolice(),
					seguro.getStatusSeguro().name(),
					seguro.getDataAcionamento(),
					seguro.getValorFraude()
			);
			seguro.setId(id);
			log.info(ConstantUtils.SUCESSO_CRIAR_SEGURO_BANCO_DADOS);
			return seguro;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_CRIAR_SEGURO_BANCO_DADOS, e);
			throw new SystemException(ConstantUtils.ERRO_CRIAR_SEGURO_BANCO_DADOS);
		}
	}

	// READ - buscar seguros
	public List<Seguro> buscarTodosSeguros() {
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
		try {
			List<Seguro> seguros = jdbcTemplate.query(SqlQueries.SQL_READ_ALL_SEGUROS, seguroMapper);
			log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO);
			return seguros;
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_BUSCA_SEGURO, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_SEGURO);
		}
	}
	public Optional<Seguro> buscarSeguroPorId(Long id) {
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
		try {
			Seguro seguro = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_SEGURO_BY_ID, seguroMapper, id);
			if (seguro == null) {
				log.warn(ConstantUtils.ERRO_SEGURO_NULO);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_SEGURO + id);
			}
			log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO);
			return Optional.of(seguro);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_CARTAO, ConstantUtils.RETORNO_VAZIO);
			return Optional.empty();
		}
	}
	public List<Seguro> findByCartaoId(Long cartaoId) {
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO);
		try {
			List<Seguro> seguros = jdbcTemplate.query(SqlQueries.SQL_READ_SEGURO_BY_CARTAO, seguroMapper, cartaoId);
			log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO);
			return seguros;
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_BUSCA_SEGURO, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_SEGURO);
		}
	}
	public List<Seguro> buscarSegurosPorTipoECliente(Long clienteId, String tipoSeguro) {
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO_POR_TIPO, clienteId, tipoSeguro);
		try {
			List<Seguro> seguros = jdbcTemplate.query(SqlQueries.SQL_READ_SEGURO_BY_TIPO_CLIENTE, seguroMapper, clienteId, tipoSeguro);
			log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO_POR_TIPO, clienteId, tipoSeguro);
			return seguros;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_SEGURO_POR_TIPO, clienteId, tipoSeguro, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_SEGURO_POR_TIPO);
		}
	}
	public boolean existsByCartaoId(Long cartaoId) {
		log.info(ConstantUtils.INICIO_VERIFICAR_SEGURO_POR_CARTAO, cartaoId);
		try {
			Integer count = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_SEGURO_CARTAO, Integer.class, cartaoId);
			boolean exists = count != null && count > 0;
			log.info(ConstantUtils.SUCESSO_VERIFICAR_SEGURO_POR_CARTAO, cartaoId, exists);
			return exists;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_VERIFICAR_SEGURO_POR_CARTAO, cartaoId, e);
			throw new SystemException(ConstantUtils.ERRO_VERIFICAR_SEGURO_POR_CARTAO);
		}
	}
	public boolean existsByCartaoContaClienteId(Long clienteId) {
		log.info(ConstantUtils.INICIO_VERIFICAR_SEGURO_POR_CLIENTE, clienteId);
		try {
			Integer count = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_SEGURO_CLIENTE, Integer.class, clienteId);
			boolean exists = count != null && count > 0;
			log.info(ConstantUtils.SUCESSO_VERIFICAR_SEGURO_POR_CLIENTE, clienteId, exists);
			return exists;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_VERIFICAR_SEGURO_POR_CLIENTE, clienteId, e);
			throw new SystemException(ConstantUtils.ERRO_VERIFICAR_SEGURO_POR_CLIENTE);
		}
	}
	public List<Seguro> findSegurosByClienteId(Long clienteId) {
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO_POR_CLIENTE, clienteId);
		try {
			List<Seguro> seguros = jdbcTemplate.query(SqlQueries.SQL_READ_SEGURO_BY_CARTAO_CLIENTE_ID, seguroMapper, clienteId);
			log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO_POR_CLIENTE, clienteId);
			return seguros;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_SEGURO_POR_CLIENTE, clienteId, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_SEGURO_POR_CLIENTE);
		}
	}
	public List<Seguro> findByCartaoContaClienteUsuario(Usuario usuario) {
		log.info(ConstantUtils.INICIO_BUSCA_SEGURO_POR_USUARIO, usuario.getId());
		try {
			List<Seguro> seguros = jdbcTemplate.query(SqlQueries.SQL_READ_SEGURO_BY_CARTAO_CLIENTE_USUARIO, seguroMapper, usuario.getId());
			log.info(ConstantUtils.SUCESSO_BUSCA_SEGURO_POR_USUARIO, usuario.getId());
			return seguros;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_SEGURO_POR_USUARIO, usuario.getId(), e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_SEGURO_POR_USUARIO);
		}
	}

	// UPDATE
	public Seguro atualizarSeguro(Seguro seguro) {
		log.info(ConstantUtils.INICIO_UPDATE_SEGURO, seguro.getId());
		try{
			int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_UPDATE_SEGURO,
					seguro.getTipoSeguro().name(),
					seguro.getNumApolice(),
					seguro.getCartao().getId(),
					seguro.getDataContratacao(),
					seguro.getValorApolice(),
					seguro.getDescricaoCondicoes(),
					seguro.getPremioApolice(),
					seguro.getStatusSeguro().name(),
					seguro.getDataAcionamento(),
					seguro.getValorFraude(),
					seguro.getId()
			);
			if (linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_UPDATE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_SEGURO + seguro.getId());
			}
			log.info(ConstantUtils.SUCESSO_UPDATE_SEGURO, seguro.getId());
			return seguro;
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_UPDATE_SEGURO, e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_UPDATE_SEGURO);
		}
	}

	// DELETE
	public void deletarSeguroPorId(Long id) {
		log.info(ConstantUtils.INICIO_DELETE_SEGURO, id);
		try {
			int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_SEGURO, id);
			if (linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_DELETE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_SEGURO + id);
			}
			log.info(ConstantUtils.SUCESSO_DELETE_SEGURO, id);
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_DELETE_SEGURO, e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_DELETE_SEGURO);
		}
	}

}
