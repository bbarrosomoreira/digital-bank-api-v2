package br.com.cdb.bancodigital.application.core.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import br.com.cdb.bancodigital.utils.ConstantUtils;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoCartao {
	CREDITO(ConstantUtils.DESCRICAO_CARTAO_CREDITO),
	DEBITO(ConstantUtils.DESCRICAO_CARTAO_DEBITO);
	
	private final String descricao;

	@JsonCreator
	public static TipoCartao fromString(String tipoCartaoStr) {
		if (tipoCartaoStr == null) {
			throw new IllegalArgumentException(ConstantUtils.TIPO_CARTAO_NULO);
		}
		return Arrays.stream(TipoCartao.values())
				.filter(tipoCartao -> tipoCartao.name().equalsIgnoreCase(tipoCartaoStr))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(String.format(ConstantUtils.TIPO_CARTAO_INVALIDO, tipoCartaoStr, Arrays.toString(TipoCartao.values()))));
	}
}
