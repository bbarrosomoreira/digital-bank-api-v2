package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
	
	ADMIN,
	CLIENTE;

	@JsonCreator
	public static Role fromString(String roleStr) {
		if (roleStr == null) {
			throw new IllegalArgumentException("Role não pode ser nula.");
		}
		return Arrays.stream(Role.values())
				.filter(role -> role.name().equalsIgnoreCase(roleStr))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Valor de role inválido: " + roleStr +
						". Valores permitidos: " + Arrays.toString(Role.values())));
	}


}
