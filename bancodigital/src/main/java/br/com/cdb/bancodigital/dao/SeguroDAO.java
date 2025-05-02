package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.SeguroMapper;
import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.Usuario;
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
		String sql = """
            INSERT INTO seguro (tipo_seguro, num_apolice, cartao_id, data_contratacao,
                                valor_apolice, descricao_condicoes, premio_apolice,
                                status_seguro, data_acionamento, valor_fraude)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

		Long id = jdbcTemplate.queryForObject(sql, Long.class,
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
		String sql = "SELECT * FROM seguro";
		return jdbcTemplate.query(sql, seguroMapper);
	}
	public Optional<Seguro> buscarSeguroPorId(Long id) {
		String sql = "SELECT * FROM seguro WHERE id = ?";
		try {
			Seguro seguro = jdbcTemplate.queryForObject(sql, seguroMapper, id);
			return Optional.of(seguro);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public List<Seguro> findByCartaoId(Long cartaoId) {
		String sql = "SELECT * FROM seguro WHERE cartao_id = ?";
		return jdbcTemplate.query(sql, seguroMapper, cartaoId);
	}
	public List<Seguro> buscarSegurosPorTipoECliente(Long clienteId, String tipoSeguro) {
		String sql = """
            SELECT s.* FROM seguro s
            JOIN cartao c ON s.cartao_id = c.id
            JOIN conta ct ON c.conta_id = ct.id
            WHERE ct.cliente_id = ? AND s.tipo_seguro = ?
        """;
		return jdbcTemplate.query(sql, seguroMapper, clienteId, tipoSeguro);
	}
	// Verifica se existe seguro vinculado ao cartão
	public boolean existsByCartaoId(Long cartaoId) {
		String sql = "SELECT COUNT(*) FROM seguro WHERE cartao_id = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cartaoId);
		return count != null && count > 0;
	}
	public boolean existsByCartaoContaClienteId(Long clienteId) {
		String sql = """
            SELECT COUNT(*) FROM seguro s
            JOIN cartao c ON s.cartao_id = c.id
            JOIN conta co ON c.conta_id = co.id
            WHERE co.cliente_id = ?
        """;
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, clienteId);
		return count != null && count > 0;
	}
	public List<Seguro> findSegurosByClienteId(Long clienteId) {
		String sql = """
            SELECT s.* FROM seguro s
            JOIN cartao c ON s.cartao_id = c.id
            JOIN conta co ON c.conta_id = co.id
            WHERE co.cliente_id = ?
        """;
		return jdbcTemplate.query(sql, seguroMapper, clienteId);
	}
	public List<Seguro> findByCartaoContaClienteUsuario(Usuario usuario) {
		String sql = """
            SELECT s.* FROM seguro s
            JOIN cartao c ON s.cartao_id = c.id
            JOIN conta co ON c.conta_id = co.id
            JOIN cliente cl ON co.cliente_id = cl.id
            WHERE cl.usuario_id = ?
        """;
		return jdbcTemplate.query(sql, seguroMapper, usuario.getId());
	}

	// UPDATE
	public Seguro atualizarSeguro(Seguro seguro) {
		String sql = """
            UPDATE seguro SET tipo_seguro = ?, num_apolice = ?, cartao_id = ?, data_contratacao = ?,
                              valor_apolice = ?, descricao_condicoes = ?, premio_apolice = ?,
                              status_seguro = ?, data_acionamento = ?, valor_fraude = ?
            WHERE id = ?
        """;

		int linhasAfetadas = jdbcTemplate.update(sql,
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
		String sql = "DELETE FROM seguro WHERE id = ?";
		int linhasAfetadas = jdbcTemplate.update(sql, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Seguro com ID " + id + " não encontrado para exclusão.");
		}
	}

}
