package br.com.cdb.bancodigital.adapter.output.dao.rowMapper;

import br.com.cdb.bancodigital.adapter.output.dao.UsuarioDAO;
import br.com.cdb.bancodigital.adapter.output.model.ClienteModel;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
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
public class ClienteRowMapper implements RowMapper<ClienteModel> {

    private final UsuarioDAO usuarioDAO;

    @Override
    public ClienteModel mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        ClienteModel clienteModel = new ClienteModel();
        clienteModel.setId(rs.getLong("id"));
        clienteModel.setNome(rs.getString("nome"));
        clienteModel.setCpf(rs.getString("cpf"));
        clienteModel.setCategoria(CategoriaCliente.fromString(rs.getString("categoria")));
        clienteModel.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());

        Long usuarioId = rs.getLong("usuario_id");
        clienteModel.setUsuario(Usuario.builder().id(usuarioId).build());

        return clienteModel;
    }
}
