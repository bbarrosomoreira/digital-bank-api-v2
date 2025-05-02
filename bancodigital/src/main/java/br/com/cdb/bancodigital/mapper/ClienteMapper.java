package br.com.cdb.bancodigital.mapper;

import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ClienteMapper implements RowMapper<Cliente> {

    @Override
    public Cliente mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setCategoria(CategoriaCliente.fromString(rs.getString("categoria")));
        cliente.setDataNascimento(rs.getDate("data_nascimento"));
        cliente.setEndereco(null);
        cliente.setUsuario(null);

        return cliente;
    }
}
