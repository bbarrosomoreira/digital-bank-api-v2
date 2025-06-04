package br.com.cdb.bancodigital.application.core.domain.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class AcionarSeguroFraudeDTO {
	
	@NotNull(message = VALOR_FRAUDE_OBRIGATORIO)
	private BigDecimal valorFraude;
	
}

