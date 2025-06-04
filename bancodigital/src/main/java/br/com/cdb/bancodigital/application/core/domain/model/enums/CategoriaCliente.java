package br.com.cdb.bancodigital.application.core.domain.model.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Getter
@AllArgsConstructor
public enum CategoriaCliente {
	COMUM(ConstantUtils.DESCRICAO_CATEGORIA_COMUM, 0), //basic
	SUPER(ConstantUtils.DESCRICAO_CATEGORIA_SUPER, 1), //standard
	PREMIUM(ConstantUtils.DESCRICAO_CATEGORIA_PREMIUM, 2);
	
	private final String descricao;
	private final int nivel;
	
    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static CategoriaCliente fromString(String categoriaStr) {
        if (categoriaStr == null) {
            throw new IllegalArgumentException(ConstantUtils.CATEGORIA_NULA);
        }
        return Arrays.stream(CategoriaCliente.values())
                .filter(categoria -> categoria.name().equalsIgnoreCase(categoriaStr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(ConstantUtils.CATEGORIA_INVALIDA, categoriaStr, Arrays.toString(CategoriaCliente.values()))));
    }

}
