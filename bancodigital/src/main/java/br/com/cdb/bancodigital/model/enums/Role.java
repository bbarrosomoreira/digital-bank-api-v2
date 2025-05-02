package br.com.cdb.bancodigital.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {
	
	ADMIN,
	CLIENTE;

	public static Role fromString(String roleStr) {
		if (roleStr == null) {
			throw new IllegalArgumentException("Role não pode ser nulo.");
		}

		for (Role role : Role.values()) {
			if (role.name().equalsIgnoreCase(roleStr)) {
				return role;
			}
		}

		throw new IllegalArgumentException("Valor de role inválido: " + roleStr +
				". Valores permitidos: " + Arrays.toString(Role.values()));
	}


}
