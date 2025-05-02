package br.com.cdb.bancodigital.mapper;

import br.com.cdb.bancodigital.dao.UsuarioDAO;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class ClienteMapper implements RowMapper<Cliente> {

    private final UsuarioDAO usuarioDAO;

    @Override
    public Cliente mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setCategoria(CategoriaCliente.fromString(rs.getString("categoria")));
        cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());

        long usuarioId = rs.getLong("usuario_id");
        Usuario usuario = usuarioDAO.buscarUsuarioporId(usuarioId)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        cliente.setUsuario(usuario);

        return cliente;
    }
}
