package br.com.cdb.bancodigital.application.core.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import br.com.cdb.bancodigital.utils.ConstantUtils;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Moeda {
	BRL(ConstantUtils.DESCRICAO_MOEDA_BRL, ConstantUtils.SIMBOLO_MOEDA_BRL),
	USD(ConstantUtils.DESCRICAO_MOEDA_USD, ConstantUtils.SIMBOLO_MOEDA_USD),
	EUR(ConstantUtils.DESCRICAO_MOEDA_EUR, ConstantUtils.SIMBOLO_MOEDA_EUR);
	
	private final String nome;
	private final String simbolo;

	@JsonCreator
	public static Moeda fromString(String moedaStr) {
		if (moedaStr == null) {
			throw new IllegalArgumentException(ConstantUtils.MOEDA_NULA);
		}
		return Arrays.stream(Moeda.values())
				.filter(moeda -> moeda.name().equalsIgnoreCase(moedaStr))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(String.format(ConstantUtils.MOEDA_INVALIDA, moedaStr, Arrays.toString(Moeda.values()))));
	}

}
