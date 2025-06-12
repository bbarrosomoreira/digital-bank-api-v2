package br.com.cdb.bancodigital.application.core.domain.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Moeda;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class ConversorMoedasDTO {
	@NotNull(message = MOEDA_OBRIGATORIA)
	private Moeda moedaOrigem;
	
	@NotNull(message = MOEDA_OBRIGATORIA)
	private Moeda moedaDestino;
	
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valor;

}
