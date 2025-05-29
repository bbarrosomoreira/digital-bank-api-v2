package br.com.cdb.bancodigital.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AjustarLimiteDTO {
	
	private BigDecimal limiteNovo;
	

}
