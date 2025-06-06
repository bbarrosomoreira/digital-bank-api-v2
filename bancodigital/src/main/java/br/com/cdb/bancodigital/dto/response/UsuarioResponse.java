package br.com.cdb.bancodigital.dto.response;

import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioResponse {
    private String email;
    private Role role;

    public UsuarioResponse(Usuario usuario) {
        this.email = usuario.getEmail();
        this.role = usuario.getRole();
    }
}
