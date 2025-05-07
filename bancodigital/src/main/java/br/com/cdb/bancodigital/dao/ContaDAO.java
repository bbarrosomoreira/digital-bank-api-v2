package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.ContaMapper;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.utils.SqlQueries;
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
		Long id = jdbcTemplate.queryForObject(SqlQueries.SQL_CREATE_CONTA, Long.class,
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
		return jdbcTemplate.query(SqlQueries.SQL_READ_ALL_CONTAS, contaMapper);
	}
	public Optional<Conta> buscarContaPorId(Long id) {
		try {
			Conta conta = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CONTA_BY_ID, contaMapper, id);
			return Optional.of(conta);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	// Verifica se existe uma conta associada ao cliente (por ID)
	public boolean existsByClienteId(Long clienteId) {
		Integer count = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_CONTA, Integer.class, clienteId);
		return count != null && count > 0;
	}
	public List<Conta> buscarContaPorClienteId(Long clienteId) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_CONTA_BY_CLIENTE, contaMapper, clienteId);
	}
	public List<Conta> buscarContaPorClienteUsuario(Usuario usuario) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_CONTA_BY_CLIENTE_USUARIO, contaMapper, usuario.getId());
	}
	// Retorna contas filtradas por tipo para um cliente
	public List<Conta> buscarContasPorTipoPorCliente(Long clienteId, String tipoConta) {
		return jdbcTemplate.query(SqlQueries.SQL_READ_CONTA_BY_TIPO_CLIENTE, contaMapper, clienteId, tipoConta);
	}

	// UPDATE
	public Conta atualizarConta(Conta conta) {
		int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_UPDATE_CONTA,
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
		int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_CONTA, id);

		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Conta com ID " + id + " não encontrada para exclusão.");
		}
	}

	
}
