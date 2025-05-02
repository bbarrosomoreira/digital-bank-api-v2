package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.model.enums.Moeda;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConversorMoedasResponse {
	@NotNull(message = "Moeda da conta é obrigatório")
	private Moeda moeda;

	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorOriginal;

	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorConvertido;

}
