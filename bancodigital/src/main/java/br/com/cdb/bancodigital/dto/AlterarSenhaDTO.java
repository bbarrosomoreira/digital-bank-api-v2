package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class AlterarSenhaDTO {
	
	@NotNull
	@Size(min = 4, max = 4, message = SENHA_TAMANHO)
	private String senhaAntiga;
	
	@NotNull
	@Size(min = 4, max = 4, message = SENHA_TAMANHO)
	private String senhaNova;

}
