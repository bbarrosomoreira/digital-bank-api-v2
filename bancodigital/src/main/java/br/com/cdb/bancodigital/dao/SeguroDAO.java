package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.SeguroMapper;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeguroDAO {

	private final JdbcTemplate jdbcTemplate;
	private final SeguroMapper seguroMapper;

	// SAVE | Criar ou atualizar cartão
	public Seguro salvar(Seguro seguro) {
		if (seguro.getId() == null) {
			// Se não tiver ID, é um novo seguro (INSERT)
			return criarSeguro(seguro);
		} else {
			// Se tiver ID, é um seguro existente (UPDATE)
			return atualizarSeguro(seguro);
		}
	}

	// CREATE
	public Seguro criarSeguro(Seguro seguro) {
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
		return seguro;
	}

	// READ - buscar seguros
	public List<Seguro> buscarTodosSeguros() {
		return jdbcTemplate.query(SqlQueries.SQL_READ_ALL_SEGUROS, seguroMapper);
	}
	public Optional<Seguro> buscarSeguroPorId(Long id) {
		try {
			Seguro seguro = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_SEGURO_BY_ID, seguroMapper, id);
			return Optional.of(seguro);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public List<Seguro> findByCartaoId(Long cartaoId) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_SEGURO_BY_CARTAO, seguroMapper, cartaoId);
	}
	public List<Seguro> buscarSegurosPorTipoECliente(Long clienteId, String tipoSeguro) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_SEGURO_BY_TIPO_CLIENTE, seguroMapper, clienteId, tipoSeguro);
	}
	// Verifica se existe seguro vinculado ao cartão
	public boolean existsByCartaoId(Long cartaoId) {
		Integer count = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_SEGURO_CARTAO, Integer.class, cartaoId);
		return count != null && count > 0;
	}
	public boolean existsByCartaoContaClienteId(Long clienteId) {
		Integer count = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_SEGURO_CLIENTE, Integer.class, clienteId);
		return count != null && count > 0;
	}
	public List<Seguro> findSegurosByClienteId(Long clienteId) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_SEGURO_BY_CARTAO_CLIENTE_ID, seguroMapper, clienteId);
	}
	public List<Seguro> findByCartaoContaClienteUsuario(Usuario usuario) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_SEGURO_BY_CARTAO_CLIENTE_USUARIO, seguroMapper, usuario.getId());
	}

	// UPDATE
	public Seguro atualizarSeguro(Seguro seguro) {
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
			throw new ResourceNotFoundException("Seguro não encontrado para atualização com ID: " + seguro.getId());
		}

		return seguro;
	}

	// DELETE
	public void deletarSeguroPorId(Long id) {
		int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_SEGURO, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Seguro com ID " + id + " não encontrado para exclusão.");
		}
	}

}
