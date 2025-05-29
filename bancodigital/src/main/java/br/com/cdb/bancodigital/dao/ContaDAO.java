package br.com.cdb.bancodigital.dao;

import java.sql.CallableStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.mapper.ContaMapper;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.cdb.bancodigital.model.Conta;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContaDAO {

	private final JdbcTemplate jdbcTemplate;
	private final ContaMapper contaMapper;

	// SAVE | Criar ou atualizar conta
	public Conta salvar(Conta conta) {
		log.info(ConstantUtils.INICIO_SALVAR_CONTA);
		try{
			if (conta.getId() == null) {
				// Se não tiver ID, é uma nova conta (INSERT)
				return criarConta(conta);
			} else {
				// Se tiver ID, é uma conta existente (UPDATE)
				return atualizarConta(conta);
			}
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_SALVAR_CONTA, e);
			throw new SystemException(ConstantUtils.ERRO_SALVAR_CONTA);
		}
	}

	// CREATE
	public Conta criarConta(Conta conta) {
		log.info(ConstantUtils.INICIO_CRIAR_CONTA_BANCO_DADOS);
		try {
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
			log.info(ConstantUtils.SUCESSO_CRIAR_CONTA_BANCO_DADOS);
			return conta;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_CRIAR_CONTA_BANCO_DADOS, e);
			throw new SystemException(ConstantUtils.ERRO_CRIAR_CONTA_BANCO_DADOS);
		}
	}

	// READ - listar todas
	public List<Conta> buscarTodasContas() {
		log.info(ConstantUtils.INICIO_BUSCA_CONTA);
		try {
			List<Conta> contas = jdbcTemplate.query(SqlQueries.SQL_READ_ALL_CONTAS, contaMapper);
			log.info(ConstantUtils.SUCESSO_BUSCA_CONTA);
			return contas;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CONTA, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CONTA);
		}
	}
	public Optional<Conta> buscarContaPorId(Long id) {
		log.info(ConstantUtils.INICIO_BUSCA_CONTA);
		try {
			Conta conta = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CONTA_BY_ID, contaMapper, id);
			if (conta == null) {
				log.warn(ConstantUtils.ERRO_CONTA_NULA);
				return Optional.empty();
			}
			log.info(ConstantUtils.SUCESSO_BUSCA_CONTA);
			return Optional.of(conta);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_CONTA, ConstantUtils.RETORNO_VAZIO);
			return Optional.empty();
		}
	}
	public List<Conta> buscarContaPorClienteId(Long clienteId) {
		log.info(ConstantUtils.INICIO_BUSCA_CONTA_POR_CLIENTE, clienteId);
		try {
			List<Conta> contas = jdbcTemplate.query(SqlQueries.SQL_READ_CONTA_BY_CLIENTE, contaMapper, clienteId);
			log.info(ConstantUtils.SUCESSO_BUSCA_CONTA_POR_CLIENTE, clienteId);
			return contas;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CONTA_POR_CLIENTE, clienteId, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CONTA_POR_CLIENTE);
		}
	}
	public List<Conta> buscarContaPorClienteUsuario(Usuario usuario) {
		log.info(ConstantUtils.INICIO_BUSCA_CONTA_POR_USUARIO, usuario.getId());
		try {
			List<Conta> contas = jdbcTemplate.query(SqlQueries.SQL_READ_CONTA_BY_CLIENTE_USUARIO, contaMapper, usuario.getId());
			log.info(ConstantUtils.SUCESSO_BUSCA_CONTA_POR_USUARIO, usuario.getId());
			return contas;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CONTA_POR_USUARIO, usuario.getId(), e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CONTA_POR_USUARIO);
		}
	}

	public void validarVinculosConta(Long id) {
		log.info(ConstantUtils.INICIO_VALIDAR_VINCULOS_CONTA, id);
		jdbcTemplate.call(con -> {
			CallableStatement cs = con.prepareCall(SqlQueries.SQL_VALIDAR_VINCULOS_CONTA);
			cs.setLong(1, id);
			return cs;
		}, Collections.emptyList());
	}

	// UPDATE
	public Conta atualizarConta(Conta conta) {
		log.info(ConstantUtils.INICIO_UPDATE_CONTA, conta.getId());
		try {
			Integer linhasAfetadas = jdbcTemplate.queryForObject(
					SqlQueries.SQL_UPDATE_CONTA,
					Integer.class,
					conta.getId(),
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
			if (linhasAfetadas == null || linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_UPDATE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CONTA + conta.getId());
			}
			log.info(ConstantUtils.SUCESSO_UPDATE_CONTA, conta.getId());
			return conta;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_UPDATE_CONTA, e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_UPDATE_CONTA);
		}
	}

	// DELETE
	public void deletarContaPorId(Long id) {
		log.info(ConstantUtils.INICIO_DELETE_CONTA, id);
		try {
			Integer linhasAfetadas = jdbcTemplate.queryForObject(
					SqlQueries.SQL_DELETE_CONTA,
					Integer.class,
					id);
			if (linhasAfetadas == null || linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_DELETE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CONTA);
			}
			log.info(ConstantUtils.SUCESSO_DELETE_CONTA, id);
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_DELETE_CONTA, id, e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_DELETE_CONTA + id);
		}
	}

	
}
