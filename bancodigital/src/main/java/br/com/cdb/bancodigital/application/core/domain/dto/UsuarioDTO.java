package br.com.cdb.bancodigital.application.core.domain.dto;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {
	@NotBlank(message = EMAIL_OBRIGATORIO)
	@Email
	private String email;
	
	@NotBlank(message = SENHA_OBRIGATORIA)
	@Size(min = 6)
	private String senha;
	
	@NotNull
	private Role role;

}
