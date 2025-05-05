package br.com.cdb.bancodigital.model.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoriaCliente {
	COMUM("Comum", 0), //basic
	SUPER("Super", 1), //standard
	PREMIUM("Premium", 2);
	
	private final String descricao;
	private final int nivel;
	
    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static CategoriaCliente fromString(String categoriaStr) {
        if (categoriaStr == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula.");
        }
        return Arrays.stream(CategoriaCliente.values())
                .filter(categoria -> categoria.name().equalsIgnoreCase(categoriaStr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida: " + categoriaStr +
                        ". Valores permitidos: " + Arrays.toString(CategoriaCliente.values())));
    }

}
