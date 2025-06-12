package br.com.cdb.bancodigital.adapter.output.dao;

import br.com.cdb.bancodigital.application.port.out.repository.EnderecoClienteRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.config.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.adapter.output.dao.rowMapper.EnderecoClienteRowMapper;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.EnderecoCliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnderecoClienteDAO implements EnderecoClienteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EnderecoClienteRowMapper enderecoClienteMapper;

    // SAVE | Criar ou atualizar endereço
    public EnderecoCliente save(EnderecoCliente endereco) {
        log.info(ConstantUtils.INICIO_SALVAR_ENDERECO);
        try {
            if (endereco.getId() == null) {
                // Se não tiver ID, é um novo endereço (INSERT)
                return add(endereco);
            } else {
                // Se tiver ID, é um endereço existente (UPDATE)
                return update(endereco);
            }
        } catch (SystemException e) {
            log.error(ConstantUtils.ERRO_SALVAR_ENDERECO, e);
            throw new SystemException(ConstantUtils.ERRO_SALVAR_ENDERECO);
        }
    }

    // CREATE | Criar endereço
    public EnderecoCliente add(EnderecoCliente endereco) {
        log.info(ConstantUtils.INICIO_CRIAR_ENDERECO);
        try {
            Long id = jdbcTemplate.queryForObject(
                    SqlQueries.SQL_CREATE_ENDERECO_CLIENTE,
                    Long.class,
                    endereco.getCep(),
                    endereco.getRua(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getEstado(),
                    endereco.getCliente().getId()
            );
            endereco.setId(id);
            log.info(ConstantUtils.SUCESSO_CRIAR_ENDERECO);
            return endereco;
        } catch (SystemException e) {
            log.error(ConstantUtils.ERRO_CRIAR_ENDERECO, e);
            throw new SystemException(ConstantUtils.ERRO_CRIAR_ENDERECO);
        }
    }

    // READ | Listar endereço
    public Optional<EnderecoCliente> findByCliente(Cliente cliente) {
        log.info(ConstantUtils.INICIO_BUSCA_ENDERECO, cliente.getId());
        try {
            EnderecoCliente endereco = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_ENDERECO_CLIENTE_BY_CLIENTE, enderecoClienteMapper, cliente.getId());
            if (endereco == null) {
                log.warn(ConstantUtils.ERRO_ENDERECO_NULO);
                return Optional.empty();
            }
            log.info(ConstantUtils.ENDERECO_ENCONTRADO, endereco.getId());
            return Optional.of(endereco);
        } catch (EmptyResultDataAccessException e) {
            log.warn("{} - {}", ConstantUtils.ERRO_BUSCA_ENDERECO, ConstantUtils.RETORNO_VAZIO);
            return Optional.empty();
        }
    }
    // UPDATE | Atualizar endereço
    public EnderecoCliente update(EnderecoCliente endereco) {
        log.info(ConstantUtils.INICIO_UPDATE_ENDERECO);
        try {
            Integer linhasAfetadas = jdbcTemplate.queryForObject(
                    SqlQueries.SQL_UPDATE_ENDERECO_CLIENTE,
                    Integer.class,
                    endereco.getId(),
                    endereco.getCep(),
                    endereco.getRua(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getEstado(),
                    endereco.getCliente().getId()
            );
            if (linhasAfetadas == null || linhasAfetadas == 0) {
                log.warn(ConstantUtils.ERRO_UPDATE);
                throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_ENDERECO);
            }
            log.info(ConstantUtils.SUCESSO_UPDATE_ENDERECO);
            return endereco;
        } catch (SystemException e) {
            log.error(ConstantUtils.ERRO_INESPERADO_UPDATE_ENDERECO,endereco.getId(), e);
            throw new SystemException(ConstantUtils.ERRO_INESPERADO_UPDATE_ENDERECO + endereco.getId());
        }

    }




}
