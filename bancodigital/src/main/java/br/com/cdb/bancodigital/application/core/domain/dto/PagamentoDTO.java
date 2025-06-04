package br.com.cdb.bancodigital.application.core.domain.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoDTO {
	
	@Positive(message = VALOR_POSITIVO)
	private BigDecimal valor;
	
	@Size(max = 100, message = DESCRICAO_TAMANHO)
	private String descricao;
	
	@NotNull
	@Size(min = 4, max = 4, message = SENHA_TAMANHO)
	private String senha;
	
}

