package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlterarSenhaDTO {
	
	@NotNull
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senhaAntiga;
	
	@NotNull
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senhaNova;

}
