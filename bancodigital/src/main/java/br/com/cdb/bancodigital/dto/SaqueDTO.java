package br.com.cdb.bancodigital.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaqueDTO {
	
	@Positive(message = "O valor deve ser positivo")
	@DecimalMin(value = "1.00", message = "O valor mínimo é R$1,00")
	private BigDecimal valor;
}