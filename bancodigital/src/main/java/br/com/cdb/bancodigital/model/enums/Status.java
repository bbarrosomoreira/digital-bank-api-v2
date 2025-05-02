package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
	ATIVO(1),
	INATIVO(2);
	
	private final int codigo;
	
    @JsonCreator
    public static Status fromString(String statusStr) {
        return Status.valueOf(statusStr.toUpperCase());
    }
}
