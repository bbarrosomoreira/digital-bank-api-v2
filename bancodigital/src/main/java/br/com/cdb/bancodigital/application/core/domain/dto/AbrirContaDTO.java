package br.com.cdb.bancodigital.application.core.domain.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoConta;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class AbrirContaDTO {
	
	private Long id_cliente;
	
	@NotNull(message = TIPO_CONTA_OBRIGATORIO)
	private TipoConta tipoConta;
	
	@NotNull(message = MOEDA_CONTA_OBRIGATORIO)
	private Moeda moeda;
	
	@Digits(integer = 19, fraction = 2)
	private BigDecimal valorDeposito;
}

