package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.EnderecoClienteMapper;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.EnderecoCliente;
import br.com.cdb.bancodigital.utils.SqlQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnderecoClienteDAO {

    private final JdbcTemplate jdbcTemplate;
    private final EnderecoClienteMapper enderecoClienteMapper;

    // SAVE | Criar ou atualizar endereço
    public EnderecoCliente salvar(EnderecoCliente endereco) {
        if (endereco.getId() == null) {
            // Se não tiver ID, é um novo endereço (INSERT)
            return criarEndereco(endereco);
        } else {
            // Se tiver ID, é um endereço existente (UPDATE)
            return atualizarEndereco(endereco);
        }
    }

    // CREATE | Criar endereço
    public EnderecoCliente criarEndereco(EnderecoCliente endereco) {
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
        return endereco;
    }

    // READ | Listar endereço
    public List<EnderecoCliente> listarEnderecosPorCliente(Long clienteId) {
        return jdbcTemplate.query(SqlQueries.SQL_READ_ENDERECO_CLIENTE_BY_CLIENTE, enderecoClienteMapper, clienteId);
    }

    public Optional<EnderecoCliente> buscarEnderecoporCliente(Cliente cliente) {
        try {
            EnderecoCliente endereco = jdbcTemplate.queryForObject(SqlQueries.SQL_READ_ENDERECO_CLIENTE_BY_CLIENTE, enderecoClienteMapper, cliente.getId());
            return Optional.of(endereco);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public EnderecoCliente buscarEnderecoporClienteOuErro(Cliente cliente) {
        return buscarEnderecoporCliente(cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco não encontrado"));
    }

    // UPDATE | Atualizar endereço
    public EnderecoCliente atualizarEndereco(EnderecoCliente endereco) {
        int linhasAfetadas = jdbcTemplate.update(SqlQueries.SQL_UPDATE_ENDERECO_CLIENTE,
                endereco.getCep(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getId()
        );

        if (linhasAfetadas == 0) {
            throw new ResourceNotFoundException("Endereço não encontrado com ID: " + endereco.getId());
        }

        return endereco;
    }




}
