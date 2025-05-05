package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Status {
	ATIVO(1),
	INATIVO(2);
	
	private final int codigo;

    @JsonCreator
    public static Status fromString(String statusStr) {
        if (statusStr == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }
        return Arrays.stream(Status.values())
                .filter(status -> status.name().equalsIgnoreCase(statusStr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status inválido: " + statusStr +
                        ". Valores permitidos: " + Arrays.toString(Status.values())));
    }
}
