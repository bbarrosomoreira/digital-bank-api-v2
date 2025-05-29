package br.com.cdb.bancodigital.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class DepositoDTO {
	
	@Positive(message = VALOR_POSITIVO)
	@DecimalMin(value = VALOR_MIN_DEPOSITO, message = VALOR_MINIMO)
	private BigDecimal valor;

}
