package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.model.enums.TipoCartao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class EmitirCartaoDTO {
	
	private Long id_conta;
	
	@NotNull(message = TIPO_CARTAO_OBRIGATORIO)
	private TipoCartao tipoCartao;
	
	@NotNull
	@Size(min = 4, max = 4, message = SENHA_TAMANHO)
	private String senha;

}
