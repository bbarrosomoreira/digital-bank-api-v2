package br.com.cdb.bancodigital.application.core.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import br.com.cdb.bancodigital.utils.ConstantUtils;

@Getter
@AllArgsConstructor
public enum TipoConta {
	CORRENTE(ConstantUtils.DESCRICAO_CONTA_CORRENTE),
	POUPANCA(ConstantUtils.DESCRICAO_CONTA_POUPANCA),
	INTERNACIONAL(ConstantUtils.DESCRICAO_CONTA_INTERNACIONAL);
	
	private final String descricao;

	@JsonCreator
	public static TipoConta fromString(String tipoContaStr) {
		if (tipoContaStr == null) {
			throw new IllegalArgumentException(ConstantUtils.TIPO_CONTA_NULO);
		}
		return Arrays.stream(TipoConta.values())
				.filter(tipoConta -> tipoConta.name().equalsIgnoreCase(tipoContaStr))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(String.format(ConstantUtils.TIPO_CONTA_INVALIDO, tipoContaStr, Arrays.toString(TipoConta.values()))));
	}

}
