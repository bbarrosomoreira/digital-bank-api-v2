package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.CartaoMapper;
import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Usuario;
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
		String sql = """
            INSERT INTO cartao (tipo_cartao, numero_cartao, conta_id, status, senha,
                                data_emissao, data_vencimento, taxa_utilizacao,
                                limite, limite_atual, total_fatura, total_fatura_paga)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

		Long id = jdbcTemplate.queryForObject(sql, Long.class,
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
		String sql = "SELECT * FROM cartao";
		return jdbcTemplate.query(sql, cartaoMapper);
	}
	public Optional<Cartao> findCartaoById(Long id) {
		String sql = "SELECT * FROM cartao WHERE id = ?";
		try {
			Cartao cartao = jdbcTemplate.queryForObject(sql, cartaoMapper, id);
			return Optional.of(cartao);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public List<Cartao> findByContaId(Long contaId) {
		String sql = "SELECT * FROM cartao WHERE conta_id = ?";
		return jdbcTemplate.query(sql, cartaoMapper, contaId);
	}
	public boolean existsByContaId(Long contaId) {
		String sql = "SELECT COUNT(*) FROM cartao WHERE conta_id = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, contaId);
		return count != null && count > 0;
	}
	public boolean existsByContaClienteId(Long clienteId) {
		String sql = """
            SELECT COUNT(*) FROM cartao cartao
            JOIN conta conta ON cartao.conta_id = conta.id
            WHERE conta.cliente_id = ?
        """;
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, clienteId);
		return count != null && count > 0;
	}
	public boolean existsByNumeroCartao(String numeroCartao) {
		String sql = "SELECT COUNT(*) FROM cartao WHERE numero_cartao = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, numeroCartao);
		return count != null && count > 0;
	}
	public List<Cartao> findByContaClienteUsuario(Usuario usuario) {
		String sql = """
            SELECT c.* FROM cartao c
            JOIN conta conta ON c.conta_id = conta.id
            JOIN cliente cliente ON conta.cliente_id = cliente.id
            WHERE cliente.usuario_id = ?
        """;
		return jdbcTemplate.query(sql, cartaoMapper, usuario.getId());
	}
	public List<Cartao> findByContaClienteId(Long clienteId) {
		String sql = """
            SELECT c.* FROM cartao c
            JOIN conta conta ON c.conta_id = conta.id
            WHERE conta.cliente_id = ?
        """;
		return jdbcTemplate.query(sql, cartaoMapper, clienteId);
	}
	// Listar cartões por tipo para um cliente
	public List<Cartao> buscarCartoesPorTipoECliente(Long clienteId, String tipoCartao) {
		String sql = """
            SELECT c.* FROM cartao c
            JOIN conta conta ON c.conta_id = conta.id
            WHERE conta.cliente_id = ? AND c.tipo_cartao = ?
        """;
		return jdbcTemplate.query(sql, cartaoMapper, clienteId, tipoCartao);
	}

	// UPDATE
	public Cartao atualizarCartao(Cartao cartao) {
		String sql = """
            UPDATE cartao SET tipo_cartao = ?, numero_cartao = ?, conta_id = ?, status = ?, senha = ?,
                              data_emissao = ?, data_vencimento = ?, taxa_utilizacao = ?,
                              limite = ?, limite_atual = ?, total_fatura = ?, total_fatura_paga = ?
            WHERE id = ?
        """;

		int linhasAfetadas = jdbcTemplate.update(sql,
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
		String sql = "DELETE FROM cartao WHERE id = ?";
		int linhasAfetadas = jdbcTemplate.update(sql, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Cartão com ID " + id + " não encontrado para exclusão.");
		}
	}
	
}
