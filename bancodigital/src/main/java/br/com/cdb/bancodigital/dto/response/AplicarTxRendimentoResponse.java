package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AplicarTxRendimentoResponse {
	private String numConta;
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valor;
	@Digits(integer = 19, fraction = 2)
	private BigDecimal saldo;

	public static AplicarTxRendimentoResponse toAplicarTxRendimentoResponse(String numConta, BigDecimal valor, BigDecimal saldo) {
		return new AplicarTxRendimentoResponse(
				numConta,
				valor,
				saldo
				);
	}

}