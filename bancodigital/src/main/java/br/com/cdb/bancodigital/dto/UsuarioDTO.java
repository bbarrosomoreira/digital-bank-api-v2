package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 6)
	private String senha;
	
	@NotNull
	private Role role;

}
