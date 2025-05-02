package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.ContaMapper;
import br.com.cdb.bancodigital.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.cdb.bancodigital.model.Conta;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContaDAO {

	private final JdbcTemplate jdbcTemplate;
	private final ContaMapper contaMapper;

	// SAVE | Criar ou atualizar conta
	public Conta salvar(Conta conta) {
		if (conta.getId() == null) {
			// Se não tiver ID, é uma nova conta (INSERT)
			return criarConta(conta);
		} else {
			// Se tiver ID, é uma conta existente (UPDATE)
			return atualizarConta(conta);
		}
	}

	// CREATE
	public Conta criarConta(Conta conta) {
		String sql = """
            INSERT INTO conta (numero_conta, saldo, moeda, cliente_id, data_criacao,
                               tipo_conta, tarifa_manutencao, taxa_rendimento, saldo_em_reais)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

		Long id = jdbcTemplate.queryForObject(sql, Long.class,
				conta.getNumeroConta(),
				conta.getSaldo(),
				conta.getMoeda().name(),
				conta.getCliente().getId(),
				conta.getDataCriacao(),
				conta.getTipoConta().name(),
				conta.getTarifaManutencao(),
				conta.getTaxaRendimento(),
				conta.getSaldoEmReais()
		);

		conta.setId(id);
		return conta;
	}

	// READ - listar todas
	public List<Conta> buscarTodasContas() {
		String sql = "SELECT * FROM conta";
		return jdbcTemplate.query(sql, contaMapper);
	}
	public Optional<Conta> buscarContaPorId(Long id) {
		String sql = "SELECT * FROM conta WHERE id = ?";
		try {
			Conta conta = jdbcTemplate.queryForObject(sql, contaMapper, id);
			return Optional.of(conta);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	public Optional<Conta> buscarPorNumeroConta(String numeroConta) {
		String sql = "SELECT * FROM conta WHERE numero_conta = ?";
		try {
			Conta conta = jdbcTemplate.queryForObject(sql, contaMapper, numeroConta);
			return Optional.of(conta);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	// Verifica se existe uma conta associada ao cliente (por ID)
	public boolean existsByClienteId(Long clienteId) {
		String sql = "SELECT COUNT(*) FROM conta WHERE cliente_id = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, clienteId);
		return count != null && count > 0;
	}
	public List<Conta> buscarPorClienteId(Long clienteId) {
		String sql = "SELECT * FROM conta WHERE cliente_id = ?";
		return jdbcTemplate.query(sql, contaMapper, clienteId);
	}
	public List<Conta> buscarPorClienteUsuario(Usuario usuario) {
		String sql = """
        SELECT c.* FROM conta c
        JOIN cliente cliente ON c.cliente_id = cl.id
        WHERE cliente.usuario_id = ?
    """;
		return jdbcTemplate.query(sql, contaMapper, usuario.getId());
	}
	// Retorna contas filtradas por tipo para um cliente
	public List<Conta> buscarContasPorTipoPorCliente(Long clienteId, String tipoConta) {
		String sql = "SELECT * FROM conta WHERE cliente_id = ? AND tipo_conta = ?";
		return jdbcTemplate.query(sql, contaMapper, clienteId, tipoConta);
	}

	// UPDATE
	public Conta atualizarConta(Conta conta) {
		String sql = """
            UPDATE conta SET numero_conta = ?, saldo = ?, moeda = ?, cliente_id = ?,
                             data_criacao = ?, tipo_conta = ?, tarifa_manutencao = ?, 
                             taxa_rendimento = ?, saldo_em_reais = ? WHERE id = ?
        """;

		int linhasAfetadas = jdbcTemplate.update(sql,
				conta.getNumeroConta(),
				conta.getSaldo(),
				conta.getMoeda().name(),
				conta.getCliente().getId(),
				conta.getDataCriacao(),
				conta.getTipoConta().name(),
				conta.getTarifaManutencao(),
				conta.getTaxaRendimento(),
				conta.getSaldoEmReais(),
				conta.getId()
		);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Conta não encontrada para atualização com ID: " + conta.getId());
		}

		return conta;
	}

	// DELETE
	public void deletarContaPorId(Long id) {
		String sql = "DELETE FROM conta WHERE id = ?";
		int linhasAfetadas = jdbcTemplate.update(sql, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Conta com ID " + id + " não encontrada para exclusão.");
		}
	}

	
}
