package br.com.cdb.bancodigital.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoSeguro {
	VIAGEM ("Seguro de Viagem", "Cobertura para despesas médicas no exterior, cancelamento de voos e extravio de bagagem, com um valor base de R$10.000,00.", "Clientes Comum e Super: opcional por R$50,00 por mês. Clientes Premium: isento de tarifa."),
	FRAUDE ("Seguro de Fraude", "Cobertura automática para fraudes no cartão, com um valor base de R$5.000,00", "Serviço gratuito para todas as categorias de clientes.");
	
	private final String nome;
	private final String descricao;
	private final String condicoes;

	@JsonCreator
	public static TipoSeguro fromString(String tipoSeguroStr) {
		if (tipoSeguroStr == null) {
			throw new IllegalArgumentException("Tipo de seguro não pode ser nulo.");
		}
		return Arrays.stream(TipoSeguro.values())
				.filter(tipoSeguro -> tipoSeguro.name().equalsIgnoreCase(tipoSeguroStr))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Tipo de seguro inválido: " + tipoSeguroStr +
						". Valores permitidos: " + Arrays.toString(TipoSeguro.values())));
	}

}
