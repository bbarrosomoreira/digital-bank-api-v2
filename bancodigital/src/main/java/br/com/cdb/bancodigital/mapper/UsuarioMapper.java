package br.com.cdb.bancodigital.mapper;

import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class UsuarioMapper implements RowMapper<Usuario> {

    @Override
    public Usuario mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setRole(Role.fromString(rs.getString("role")));
        return usuario;
    }
}
