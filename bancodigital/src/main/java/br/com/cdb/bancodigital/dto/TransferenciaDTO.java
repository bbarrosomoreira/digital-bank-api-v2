package br.com.cdb.bancodigital.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class TransferenciaDTO {
	
	@NotNull(message = ID_CONTA_DESTINO_OBRIGATORIO)
	private Long id_contaDestino;
	
	@Positive(message = VALOR_POSITIVO)
	@DecimalMin(value = VALOR_MIN_TRANSFERENCIA, message = VALOR_MINIMO)
	private BigDecimal valor;
	
	@JsonFormat(pattern = FORMATO_DATA_DD_MM_YYYY)
	private LocalDate dataTransacao;

}
