package br.com.cdb.bancodigital.adapter.output.dao.rowMapper;

import br.com.cdb.bancodigital.adapter.output.dao.UsuarioDAO;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class ClienteRowMapper implements RowMapper<Cliente> {

    private final UsuarioDAO usuarioDAO;

    @Override
    public Cliente mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setCategoria(CategoriaCliente.fromString(rs.getString("categoria")));
        cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());

        Long usuarioId = rs.getLong("usuario_id");
        Usuario usuario = usuarioDAO.findById(usuarioId)
                        .orElseThrow(() -> new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_USUARIO));
        cliente.setUsuario(usuario);

        return cliente;
    }
}
