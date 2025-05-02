package br.com.cdb.bancodigital.dao;

import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.mapper.EnderecoClienteMapper;
import br.com.cdb.bancodigital.model.EnderecoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoClienteDAO {

    private final JdbcTemplate jdbcTemplate;
    private final EnderecoClienteMapper enderecoClienteMapper;

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
