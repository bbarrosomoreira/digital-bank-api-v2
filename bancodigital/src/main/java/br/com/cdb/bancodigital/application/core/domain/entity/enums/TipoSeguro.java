package br.com.cdb.bancodigital.application.core.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import br.com.cdb.bancodigital.utils.ConstantUtils;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoSeguro {
	VIAGEM(
		ConstantUtils.NOME_SEGURO_VIAGEM,
		ConstantUtils.DESCRICAO_SEGURO_VIAGEM,
		ConstantUtils.CONDICOES_SEGURO_VIAGEM
	),
	FRAUDE(
		ConstantUtils.NOME_SEGURO_FRAUDE,
		ConstantUtils.DESCRICAO_SEGURO_FRAUDE,
		ConstantUtils.CONDICOES_SEGURO_FRAUDE
	);
	
	private final String nome;
	private final String descricao;
	private final String condicoes;

	@JsonCreator
	public static TipoSeguro fromString(String tipoSeguroStr) {
		if (tipoSeguroStr == null) {
			throw new IllegalArgumentException(ConstantUtils.TIPO_SEGURO_NULO);
		}
		return Arrays.stream(TipoSeguro.values())
				.filter(tipoSeguro -> tipoSeguro.name().equalsIgnoreCase(tipoSeguroStr))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(String.format(ConstantUtils.TIPO_SEGURO_INVALIDO, tipoSeguroStr, Arrays.toString(TipoSeguro.values()))));
	}

}
