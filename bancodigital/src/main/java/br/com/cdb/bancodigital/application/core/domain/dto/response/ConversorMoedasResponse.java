package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Moeda;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConversorMoedasResponse {
	@NotNull(message = ConstantUtils.MOEDA_CONTA_OBRIGATORIO)
	private Moeda moeda;

	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorOriginal;

	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorConvertido;

}
