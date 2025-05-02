package br.com.cdb.bancodigital.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcionarSeguroFraudeDTO {
	
	@NotNull(message = "Valor da fraude é obrigatório")
	private BigDecimal valorFraude;
	
}
