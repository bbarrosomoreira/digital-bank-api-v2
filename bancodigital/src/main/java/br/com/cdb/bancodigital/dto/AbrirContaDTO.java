package br.com.cdb.bancodigital.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AbrirContaDTO {
	
	private Long id_cliente;
	
	@NotNull(message = "Tipo de conta é obrigatório")
	private TipoConta tipoConta;
	
	@NotNull(message = "Moeda da conta é obrigatório")
	private Moeda moeda;
	
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorDeposito;
}
