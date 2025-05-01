package br.com.cdb.bancodigitaljpa.model.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoriaCliente {
	COMUM("Comum", 0), //basic
	SUPER("Super", 1), //standard
	PREMIUM("Premium", 2);
	
	private final String descricao;
	private final int nivel;
	
	CategoriaCliente(String descricao, int nivel){
		this.descricao = descricao;
		this.nivel = nivel;
	}
	
    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static CategoriaCliente fromString(String value) {
        for (CategoriaCliente categoria : CategoriaCliente.values()) {
            if (categoria.name().equalsIgnoreCase(value)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria inválida. Valores permitidos: " +
                Arrays.toString(CategoriaCliente.values()));
    }
	
	public int getNivel() {
		return nivel;
	}

}
