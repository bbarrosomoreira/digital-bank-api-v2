package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.EnderecoClienteMapper;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.EnderecoCliente;
import br.com.cdb.bancodigital.model.Usuario;
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
        String sql = "INSERT INTO endereco_cliente (cep, rua, numero, complemento, bairro, cidade, estado, cliente_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class,
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
        String sql = "SELECT * FROM endereco_cliente WHERE cliente_id = ?";
        return jdbcTemplate.query(sql, enderecoClienteMapper, clienteId);
    }

    public Optional<EnderecoCliente> buscarEnderecoporCliente(Cliente cliente) {
        String sql = "SELECT * FROM endereco_cliente WHERE cliente_id = ?";
        try {
            EnderecoCliente endereco = jdbcTemplate.queryForObject(sql, enderecoClienteMapper, cliente.getId());
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
        String sql = "UPDATE endereco_cliente SET cep = ?, rua = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ?" +
                "WHERE id = ?";
        int linhasAfetadas = jdbcTemplate.update(sql,
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
