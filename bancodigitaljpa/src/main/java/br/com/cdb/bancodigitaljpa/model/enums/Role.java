package br.com.cdb.bancodigitaljpa.model.enums;

public enum Role {
	
	ADMIN,
	CLIENTE;
	
	Role() {}

	public static Role fromString(String roleStr) {
		if (roleStr == null) {
			return null;
		}

		try {
			return Role.valueOf(roleStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Valor de role inválido: " + roleStr);
		}
	}


}
