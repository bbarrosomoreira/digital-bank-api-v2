package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.CommunicationException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.mapper.ClienteMapper;
import br.com.cdb.bancodigital.model.Cliente;
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
public class ClienteDAO {

	private final JdbcTemplate jdbcTemplate;
	private final ClienteMapper clienteMapper;

	// SAVE | Criar ou atualizar cliente
	public Cliente salvar(Cliente cliente) {
		log.info(ConstantUtils.INICIO_SALVAR_CLIENTE);
		try {
			if (cliente.getId() == null) {
				// Se não tiver ID, é um novo cliente (INSERT)
				return criarCliente(cliente);
			} else {
				// Se tiver ID, é um cliente existente (UPDATE)
				return atualizarCliente(cliente);
			}
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_SALVAR_CLIENTE, e);
			throw new SystemException(ConstantUtils.ERRO_SALVAR_CLIENTE);
		}
	}

	// CREATE | Criar cliente
	public Cliente criarCliente(Cliente cliente) {
		log.info(ConstantUtils.INICIO_CRIAR_CLIENTE_BANCO_DADOS);
		try {
			Long id = jdbcTemplate.queryForObject(
					SqlQueries.SQL_CREATE_CLIENTE,
					Long.class,
					cliente.getNome(),
					cliente.getCpf(),
					cliente.getCategoria().name(),
					cliente.getDataNascimento(),
					cliente.getUsuario().getId()
			);
			cliente.setId(id);
			log.info(ConstantUtils.SUCESSO_CRIAR_CLIENTE_BANCO_DADOS);
			return cliente;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_CRIAR_CLIENTE_BANCO_DADOS, e);
			throw new SystemException(ConstantUtils.ERRO_CRIAR_CLIENTE_BANCO_DADOS);
		}
	}

	// READ | Listar clientes
	public List<Cliente> buscarTodosClientes() {
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
		try {
			List<Cliente> clientes = jdbcTemplate.query(SqlQueries.SQL_READ_ALL_CLIENTES, clienteMapper);
			log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);
			return clientes;
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CLIENTE, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CLIENTE);
		}
	}

	public Optional<Cliente> buscarClienteporId(Long id) {
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
		try {
			Cliente cliente = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CLIENTE_BY_ID, clienteMapper, id);
			if (cliente == null) {
				log.warn(ConstantUtils.ERRO_CLIENTE_NULO);
				return Optional.empty();
			}
			log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_CLIENTE, ConstantUtils.RETORNO_VAZIO);
			return Optional.empty();
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CLIENTE, id, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CLIENTE + id);
		}
	}
	public Optional<Cliente> buscarClienteporUsuario(Usuario usuario) {
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
		try {
			Cliente cliente = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CLIENTE_BY_USUARIO, clienteMapper, usuario.getId());
			if (cliente == null) {
				log.warn(ConstantUtils.ERRO_CLIENTE_NULO);
				return Optional.empty();
			}
			log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_CLIENTE, ConstantUtils.RETORNO_VAZIO);
			return Optional.empty();
		}
	}
	// Verificar se existe cliente com o CPF
	public boolean existsByCpf(String cpf) {
		log.info(ConstantUtils.INICIO_VERIFICAR_CLIENTE_CPF);
		// O resultado será o número de registros encontrados (1 ou mais)
		try {
			Boolean exists = jdbcTemplate.queryForObject(SqlQueries.SQL_COUNT_CLIENTE, Boolean.class, cpf);
			log.info(ConstantUtils.SUCESSO_VERIFICAR_CLIENTE_CPF);
			return Boolean.TRUE.equals(exists);
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_VERIFICAR_CLIENTE_CPF, e);
			throw new CommunicationException(ConstantUtils.ERRO_VERIFICAR_CLIENTE_CPF + e.getMessage());
		}
	}

	// UPDATE | Atualizar clientes
	private Cliente atualizarCliente(Cliente cliente) {
		log.info(ConstantUtils.INICIO_UPDATE_CLIENTE, cliente.getId());
		try{
			int linhasAfetadas = jdbcTemplate.update(
					SqlQueries.SQL_UPDATE_CLIENTE,
					cliente.getNome(),
					cliente.getCpf(),
					cliente.getCategoria().name(),
					cliente.getDataNascimento(),
					cliente.getId()
			);
			if (linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_UPDATE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CLIENTE);
			}
			log.info(ConstantUtils.SUCESSO_UPDATE_CLIENTE, cliente.getId());
			return cliente;
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_UPDATE_CLIENTE, cliente.getId(), e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_UPDATE_CLIENTE + cliente.getId());
		}
	}

	// DELETE | Excluir clientes
	public void deletarClientePorId(Long id) {
		log.info(ConstantUtils.INICIO_DELETE_CLIENTE, id);
		try {
			int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_DELETE_CLIENTE, id);
			if (linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_DELETE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CLIENTE);
			}
			log.info(ConstantUtils.SUCESSO_DELETE_CLIENTE, id);
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_DELETE_CLIENTE, id, e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_UPDATE_CLIENTE + id);
		}
	}

}
