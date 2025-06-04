package br.com.cdb.bancodigital.application.core.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import br.com.cdb.bancodigital.utils.ConstantUtils;

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
            throw new IllegalArgumentException(ConstantUtils.STATUS_NULO);
        }
        return Arrays.stream(Status.values())
                .filter(status -> status.name().equalsIgnoreCase(statusStr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(ConstantUtils.STATUS_INVALIDO, statusStr, Arrays.toString(Status.values()))));
    }
}

