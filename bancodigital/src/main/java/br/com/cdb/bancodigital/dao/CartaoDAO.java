package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.mapper.CartaoMapper;
import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartaoDAO {

	private final JdbcTemplate jdbcTemplate;
	private final CartaoMapper cartaoMapper;

	// SAVE | Criar ou atualizar cartão
	public Cartao salvar(Cartao cartao) {
		log.info(ConstantUtils.INICIO_SALVAR_CARTAO);
		try {
			if (cartao.getId() == null) {
				// Se não tiver ID, é um novo cartão (INSERT)
				return criarCartao(cartao);
			} else {
				// Se tiver ID, é um cartão existente (UPDATE)
				return atualizarCartao(cartao);
			}
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_SALVAR_CARTAO, e);
			throw new SystemException(ConstantUtils.ERRO_SALVAR_CARTAO);
		}
	}

	// CREATE
	public Cartao criarCartao(Cartao cartao) {
		log.info(ConstantUtils.INICIO_CRIAR_CARTAO_BANCO_DADOS);
		try {
			Long id = jdbcTemplate.queryForObject(SqlQueries.SQL_CREATE_CARTAO, Long.class,
					cartao.getTipoCartao().name(),
					cartao.getNumeroCartao(),
					cartao.getConta().getId(),
					cartao.getStatus().name(),
					cartao.getSenha(),
					cartao.getDataEmissao(),
					cartao.getDataVencimento(),
					cartao.getTaxaUtilizacao(),
					cartao.getLimite(),
					cartao.getLimiteAtual(),
					cartao.getTotalFatura(),
					cartao.getTotalFaturaPaga()
			);
			cartao.setId(id);
			log.info(ConstantUtils.SUCESSO_CRIAR_CARTAO_BANCO_DADOS);
			return cartao;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_CRIAR_CARTAO_BANCO_DADOS, e);
			throw new SystemException(ConstantUtils.ERRO_CRIAR_CARTAO_BANCO_DADOS);
		}
	}
	// READ - buscar cartões
	public List<Cartao> buscarTodosCartoes() {
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO);
		try {
			List<Cartao> cartoes = jdbcTemplate.query(SqlQueries.SQL_READ_ALL_CARTOES, cartaoMapper);
			log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO);
			return cartoes;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CARTAO, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CARTAO);
		}
	}
	public Optional<Cartao> findCartaoById(Long id) {
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO);
		try {
			Cartao cartao = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CARTAO_BY_ID, cartaoMapper, id);
			if (cartao == null) {
				log.warn(ConstantUtils.ERRO_CARTAO_NULO);
				return Optional.empty();
			}
			log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO);
			return Optional.of(cartao);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_CARTAO, ConstantUtils.RETORNO_VAZIO);
			return Optional.empty();
		}
	}
	public List<Cartao> findByContaId(Long contaId) {
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_CONTA, contaId);
		try {
			List<Cartao> cartoes = jdbcTemplate.query(SqlQueries.SQL_READ_CARTAO_BY_CONTA, cartaoMapper, contaId);
			log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO_POR_CONTA, contaId);
			return cartoes;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CARTAO_POR_CONTA, contaId, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CARTAO_POR_CONTA);
		}
	}
	public boolean existsByContaId(Long contaId) {
		log.info(ConstantUtils.INICIO_VERIFICAR_CARTAO_POR_CONTA, contaId);
		try {
			Boolean exists = jdbcTemplate.queryForObject(SqlQueries.SQL_EXIST_CARTAO_CONTA, Boolean.class, contaId);
			log.info(ConstantUtils.SUCESSO_VERIFICAR_CARTAO_POR_CONTA, contaId, exists);
			return Boolean.TRUE.equals(exists);
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_VERIFICAR_CARTAO_POR_CONTA, contaId, e);
			throw new SystemException(ConstantUtils.ERRO_VERIFICAR_CARTAO_POR_CONTA);
		}
	}
	public boolean existsByContaClienteId(Long clienteId) {
		log.info(ConstantUtils.INICIO_VERIFICAR_CARTAO_POR_CLIENTE, clienteId);
		try {
			Boolean exists = jdbcTemplate.queryForObject(SqlQueries.SQL_EXIST_CARTAO_CLIENTE, Boolean.class, clienteId);
			log.info(ConstantUtils.SUCESSO_VERIFICAR_CARTAO_POR_CLIENTE, clienteId, exists);
			return Boolean.TRUE.equals(exists);
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_VERIFICAR_CARTAO_POR_CLIENTE, clienteId, e);
			throw new SystemException(ConstantUtils.ERRO_VERIFICAR_CARTAO_POR_CLIENTE);
		}
	}
	public List<Cartao> findByContaClienteUsuario(Usuario usuario) {
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_USUARIO, usuario.getId());
		try {
			List<Cartao> cartoes = jdbcTemplate.query(SqlQueries.SQL_READ_CARTAO_BY_CONTA_CLIENTE_USUARIO, cartaoMapper, usuario.getId());
			log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO_POR_USUARIO, usuario.getId());
			return cartoes;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CARTAO_POR_USUARIO, usuario.getId(), e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CARTAO_POR_USUARIO);
		}
	}
	public List<Cartao> findByContaClienteId(Long clienteId) {
		log.info(ConstantUtils.INICIO_BUSCA_CARTAO_POR_CLIENTE, clienteId);
		try {
			List<Cartao> cartoes = jdbcTemplate.query(SqlQueries.SQL_READ_CARTAO_BY_CONTA_CLIENTE_ID, cartaoMapper, clienteId);
			log.info(ConstantUtils.SUCESSO_BUSCA_CARTAO_POR_CLIENTE, clienteId);
			return cartoes;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CARTAO_POR_CLIENTE, clienteId, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CARTAO_POR_CLIENTE);
		}
	}

	// UPDATE
	public Cartao atualizarCartao(Cartao cartao) {
		log.info(ConstantUtils.INICIO_UPDATE_CARTAO, cartao.getId());
		try {
			int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_UPDATE_CARTAO,
					cartao.getTipoCartao().name(),
					cartao.getNumeroCartao(),
					cartao.getConta().getId(),
					cartao.getStatus().name(),
					cartao.getSenha(),
					cartao.getDataEmissao(),
					cartao.getDataVencimento(),
					cartao.getTaxaUtilizacao(),
					cartao.getLimite(),
					cartao.getLimiteAtual(),
					cartao.getTotalFatura(),
					cartao.getTotalFaturaPaga(),
					cartao.getId()
			);
			if (linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_UPDATE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CARTAO + cartao.getId());
			}
			log.info(ConstantUtils.SUCESSO_UPDATE_CARTAO, cartao.getId());
			return cartao;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_UPDATE_CARTAO, cartao.getId(),e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_UPDATE_CARTAO + cartao.getId());
		}
	}

	// DELETE
	public void deletarCartaoPorId(Long id) {
		log.info(ConstantUtils.INICIO_DELETE_CARTAO, id);
		try {
			int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_CARTAO, id);
			if (linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_DELETE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CARTAO + id);
			}
			log.info(ConstantUtils.SUCESSO_DELETE_CARTAO, id);
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_DELETE_CARTAO, id, e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_DELETE_CARTAO + id);
		}
	}
}
