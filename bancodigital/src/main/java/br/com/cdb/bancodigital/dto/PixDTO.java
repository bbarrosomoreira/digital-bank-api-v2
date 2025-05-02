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

@Getter
@Setter
@NoArgsConstructor
public class PixDTO {
	
	@NotNull (message = "ID da conta de destino é obrigatório")
	private Long id_contaDestino;
	
	@Positive(message = "O valor deve ser positivo")
	@DecimalMin(value = "1.00", message = "O valor mínimo é R$1,00")
	private BigDecimal valor;
	
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataTransacao;

}
