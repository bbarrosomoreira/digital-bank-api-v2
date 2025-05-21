package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.SystemException;
import br.com.cdb.bancodigital.mapper.EnderecoClienteMapper;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.EnderecoCliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnderecoClienteDAO {

    private final JdbcTemplate jdbcTemplate;
    private final EnderecoClienteMapper enderecoClienteMapper;

    // SAVE | Criar ou atualizar endereço
    public EnderecoCliente salvar(EnderecoCliente endereco) {
        log.info(ConstantUtils.INICIO_SALVAR_ENDERECO);
        try {
            if (endereco.getId() == null) {
                // Se não tiver ID, é um novo endereço (INSERT)
                return criarEndereco(endereco);
            } else {
                // Se tiver ID, é um endereço existente (UPDATE)
                return atualizarEndereco(endereco);
            }
        } catch (SystemException e) {
            log.error(ConstantUtils.ERRO_SALVAR_ENDERECO, e);
            throw new SystemException(ConstantUtils.ERRO_SALVAR_ENDERECO);
        }
    }

    // CREATE | Criar endereço
    public EnderecoCliente criarEndereco(EnderecoCliente endereco) {
        log.info(ConstantUtils.INICIO_CRIAR_ENDERECO);
        try {
            Long id = jdbcTemplate.queryForObject(SqlQueries.SQL_CREATE_ENDERECO_CLIENTE, Long.class,
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
    public List<EnderecoCliente> listarEnderecosPorCliente(Long clienteId) {
        log.info(ConstantUtils.INICIO_BUSCA_ENDERECO, clienteId);
        return jdbcTemplate.query(SqlQueries.SQL_READ_ENDERECO_CLIENTE_BY_CLIENTE, enderecoClienteMapper, clienteId);
    }

    public Optional<EnderecoCliente> buscarEnderecoporCliente(Cliente cliente) {
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
    public EnderecoCliente buscarEnderecoporClienteOuErro(Cliente cliente) {
        log.info(ConstantUtils.INICIO_BUSCA_ERRO_ENDERECO);
        return buscarEnderecoporCliente(cliente)
                .orElseThrow(() -> {
                    log.error(ConstantUtils.ERRO_BUSCA_ENDERECO);
                    return new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_ENDERECO);
                });
    }

    // UPDATE | Atualizar endereço
    public EnderecoCliente atualizarEndereco(EnderecoCliente endereco) {
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
