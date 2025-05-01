package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.enums.Moeda;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class ConversorMoedasResponse {
	@NotNull(message = "Moeda da conta é obrigatório")
	@Enumerated(EnumType.STRING)
	private Moeda moeda;

	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorOriginal;

	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorConvertido;

	public ConversorMoedasResponse() {
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public BigDecimal getValorOriginal() {
		return valorOriginal;
	}

	public void setValorOriginal(BigDecimal valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	public BigDecimal getValorConvertido() {
		return valorConvertido;
	}

	public void setValorConvertido(BigDecimal valorConvertido) {
		this.valorConvertido = valorConvertido;
	}

}
