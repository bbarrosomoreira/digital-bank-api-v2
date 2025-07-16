package br.com.cdb.bancodigital.adapter.input.dto;


import br.com.cdb.bancodigital.application.core.domain.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;
    private String email;
    private String senha;
    private Role role;
}
