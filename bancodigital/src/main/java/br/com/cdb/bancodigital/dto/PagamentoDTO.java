package br.com.cdb.bancodigital.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoDTO {
	
	@Positive(message = "O valor deve ser positivo")
	private BigDecimal valor;
	
	@Size(max=100, message="A descrição deve ter no máximo 100 caracteres.")
	private String descricao;
	
	@NotNull
	@Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos numéricos.")
	private String senha;
	
}
