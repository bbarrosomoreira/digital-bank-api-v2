package br.com.cdb.bancodigital.adapter.output.dao;

import br.com.cdb.bancodigital.adapter.output.mapper.ClientePersistenceMapper;
import br.com.cdb.bancodigital.adapter.output.model.ClienteModel;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.CommunicationException;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.adapter.output.dao.rowMapper.ClienteRowMapper;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteDAO implements ClienteRepository {

	private final JdbcTemplate jdbcTemplate;
	private final ClienteRowMapper clienteRowMapper;
	private final ClientePersistenceMapper clientePersistenceMapper;

	// SAVE | Criar ou atualizar cliente
	@Override
	public Cliente save(Cliente cliente) {
		log.info(ConstantUtils.INICIO_SALVAR_CLIENTE);
		return cliente.getId() == null ? add(cliente) : update(cliente);
	}

	// CREATE | Criar cliente
	@Override
	public Cliente add(Cliente cliente) {
		log.info(ConstantUtils.INICIO_CRIAR_CLIENTE_BANCO_DADOS);
		try {
			ClienteModel clienteModel = clientePersistenceMapper.toModel(cliente);
			Long id = jdbcTemplate.queryForObject(
					SqlQueries.SQL_CREATE_CLIENTE,
					Long.class,
					clienteModel.getNome(),
					clienteModel.getCpf(),
					clienteModel.getCategoria().name(),
					clienteModel.getDataNascimento(),
					clienteModel.getUsuario().getId()
			);
			clienteModel.setId(id);
			log.info(ConstantUtils.SUCESSO_CRIAR_CLIENTE_BANCO_DADOS);
			return clientePersistenceMapper.toEntity(clienteModel);
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_CRIAR_CLIENTE_BANCO_DADOS, e);
			throw new SystemException(ConstantUtils.ERRO_CRIAR_CLIENTE_BANCO_DADOS);
		}
	}

	// READ | Listar clientes
	@Override
	public List<Cliente> findAll() {
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
		try {
			List<ClienteModel> clienteModels = jdbcTemplate.query(SqlQueries.SQL_READ_ALL_CLIENTES, clienteRowMapper);
			log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);
			return clienteModels.stream()
					.map(clientePersistenceMapper::toEntity)
					.toList();
		} catch (SystemException e) {
			log.error(ConstantUtils.ERRO_BUSCA_CLIENTE, e);
			throw new SystemException(ConstantUtils.ERRO_BUSCA_CLIENTE);
		}
	}
	@Override
	public Optional<Cliente> findById(Long id) {
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
		try {
			ClienteModel clienteModel = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CLIENTE_BY_ID, clienteRowMapper, id);
			if (clienteModel == null) {
				log.warn(ConstantUtils.ERRO_CLIENTE_NULO);
				return Optional.empty();
			}
			Cliente cliente = clientePersistenceMapper.toEntity(clienteModel);
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
	@Override
	public Optional<Cliente> findByUsuario(Usuario usuario) {
		log.info(ConstantUtils.INICIO_BUSCA_CLIENTE);
		try {
			ClienteModel clienteModel = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_CLIENTE_BY_USUARIO, clienteRowMapper, usuario.getId());
			if (clienteModel == null) {
				log.warn(ConstantUtils.ERRO_CLIENTE_NULO);
				return Optional.empty();
			}
			Cliente cliente = clientePersistenceMapper.toEntity(clienteModel);
			log.info(ConstantUtils.SUCESSO_BUSCA_CLIENTE);
			return Optional.of(cliente);
		} catch (EmptyResultDataAccessException e) {
			log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_CLIENTE, ConstantUtils.RETORNO_VAZIO);
			return Optional.empty();
		}
	}
	// Verificar se existe cliente com o CPF
	@Override
	public boolean existsWithCpf(String cpf) {
		log.info(ConstantUtils.INICIO_VERIFICAR_CLIENTE_CPF);
		try {
			Boolean exists = jdbcTemplate.queryForObject(SqlQueries.SQL_EXIST_CLIENTE, Boolean.class, cpf);
			log.info(ConstantUtils.SUCESSO_VERIFICAR_CLIENTE_CPF);
			return Boolean.TRUE.equals(exists);
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_VERIFICAR_CLIENTE_CPF, e);
			throw new CommunicationException(ConstantUtils.ERRO_VERIFICAR_CLIENTE_CPF + e.getMessage());
		}
	}
	@Override
	public void validateVinculosCliente(Long id) {
		log.info(ConstantUtils.INICIO_VALIDAR_VINCULOS_CLIENTE, id);
		jdbcTemplate.call(con -> {
					CallableStatement cs = con.prepareCall(SqlQueries.SQL_VALIDAR_VINCULOS_CLIENTE);
					cs.setLong(1, id);
					return cs;
				}, Collections.emptyList());
	}

	// UPDATE | Atualizar clientes
	@Override
	public Cliente update(Cliente cliente) {
		log.info(ConstantUtils.INICIO_UPDATE_CLIENTE, cliente.getId());
		try{
			ClienteModel clienteModel = clientePersistenceMapper.toModel(cliente);
			Integer linhasAfetadas = jdbcTemplate.queryForObject(
					SqlQueries.SQL_UPDATE_CLIENTE,
					Integer.class,
					clienteModel.getId(),
					clienteModel.getNome(),
					clienteModel.getCpf(),
					clienteModel.getCategoria().name(),
					clienteModel.getDataNascimento(),
					clienteModel.getUsuario().getId()

			);
			if (linhasAfetadas == null || linhasAfetadas == 0) {
				log.warn(ConstantUtils.ERRO_UPDATE);
				throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_CLIENTE);
			}
			log.info(ConstantUtils.SUCESSO_UPDATE_CLIENTE, cliente.getId());
			return clientePersistenceMapper.toEntity(clienteModel);
		} catch (DataAccessException e) {
			log.error(ConstantUtils.ERRO_INESPERADO_UPDATE_CLIENTE, cliente.getId(), e);
			throw new SystemException(ConstantUtils.ERRO_INESPERADO_UPDATE_CLIENTE + cliente.getId());
		}
	}

	// UPDATE | Atualizar categoria do cliente
	@Override
	public void updateCondicoesByCategoria(Long id, CategoriaCliente novaCategoria) {
		log.info(ConstantUtils.INICIO_UPDATE_CATEGORIA_CLIENTE, id);
		jdbcTemplate.update(SqlQueries.SQL_UPDATE_CATEGORIA_CLIENTE, id, novaCategoria.name());
	}

	// DELETE | Excluir clientes
	@Override
	public void delete(Long id) {
		log.info(ConstantUtils.INICIO_DELETE_CLIENTE, id);
		try {
			Integer linhasAfetadas = jdbcTemplate.queryForObject(
					SqlQueries.SQL_DELETE_CLIENTE,
					Integer.class,
					id);
			if (linhasAfetadas == null || linhasAfetadas == 0) {
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
