package br.com.cdb.bancodigitaljpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
	ATIVADO (1),
	DESATIVADO (2);
	
	private final int codigo;
	
	Status(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigo() {
		return codigo;
	}
	
    @JsonCreator
    public static Status fromString(String value) {
        return Status.valueOf(value.toUpperCase());
    }
}
