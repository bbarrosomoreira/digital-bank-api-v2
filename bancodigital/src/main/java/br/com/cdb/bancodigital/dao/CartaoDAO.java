package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.CartaoMapper;
import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartaoDAO {

	private final JdbcTemplate jdbcTemplate;
	private final CartaoMapper cartaoMapper;

	// SAVE | Criar ou atualizar cartão
	public Cartao salvar(Cartao cartao) {
		if (cartao.getId() == null) {
			// Se não tiver ID, é um novo cartão (INSERT)
			return criarCartao(cartao);
		} else {
			// Se tiver ID, é um cartão existente (UPDATE)
			return atualizarCartao(cartao);
		}
	}

	// CREATE
	public Cartao criarCartao(Cartao cartao) {
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
		return cartao;
	}
	// READ - buscar cartões
	public List<Cartao> buscarTodosCartoes() {
		return jdbcTemplate.query(SqlQueries.SQL_READ_ALL_CARTOES, cartaoMapper);
	}
	public Optional<Cartao> findCartaoById(Long id) {
		try {
			Cartao cartao = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CARTAO_BY_ID, cartaoMapper, id);
			return Optional.of(cartao);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public List<Cartao> findByContaId(Long contaId) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_CARTAO_BY_CONTA, cartaoMapper, contaId);
	}
	public boolean existsByContaId(Long contaId) {
		Integer count = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_CARTAO_CONTA, Integer.class, contaId);
		return count != null && count > 0;
	}
	public boolean existsByContaClienteId(Long clienteId) {
		Integer count = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_CARTAO_CLIENTE, Integer.class, clienteId);
		return count != null && count > 0;
	}
	public List<Cartao> findByContaClienteUsuario(Usuario usuario) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_CARTAO_BY_CONTA_CLIENTE_USUARIO, cartaoMapper, usuario.getId());
	}
	public List<Cartao> findByContaClienteId(Long clienteId) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_CARTAO_BY_CONTA_CLIENTE_ID, cartaoMapper, clienteId);
	}
	// Listar cartões por tipo para um cliente
	public List<Cartao> buscarCartoesPorTipoECliente(Long clienteId, String tipoCartao) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_CARTAO_BY_TIPO_CLIENTE, cartaoMapper, clienteId, tipoCartao);
	}

	// UPDATE
	public Cartao atualizarCartao(Cartao cartao) {
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
			throw new ResourceNotFoundException("Cartão não encontrado para atualização com ID: " + cartao.getId());
		}

		return cartao;
	}

	// DELETE
	public void deletarCartaoPorId(Long id) {
		int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_CARTAO, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Cartão com ID " + id + " não encontrado para exclusão.");
		}
	}
	
}
