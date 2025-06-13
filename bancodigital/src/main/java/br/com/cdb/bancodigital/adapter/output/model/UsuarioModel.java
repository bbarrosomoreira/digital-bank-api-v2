package br.com.cdb.bancodigital.adapter.output.model;


import br.com.cdb.bancodigital.application.core.domain.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioModel {
    private Long id;
    private String email;
    private String senha;
    private Role role;
}
