package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
	@NotBlank(message = EMAIL_OBRIGATORIO)
	@Email
	private String email;
	
	@NotBlank(message = SENHA_OBRIGATORIA)
	@Size(min = 6)
	private String senha;

}

