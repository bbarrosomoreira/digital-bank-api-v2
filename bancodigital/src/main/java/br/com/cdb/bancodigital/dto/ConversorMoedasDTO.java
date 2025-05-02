package br.com.cdb.bancodigital.dto;

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
public class ConversorMoedasDTO {
	@NotNull(message = "Moeda da conta é obrigatório")
	private Moeda moedaOrigem;
	
	@NotNull(message = "Moeda da conta é obrigatório")
	private Moeda moedaDestino;
	
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valor;

}
