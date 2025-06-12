package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Status;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoCartao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoResponse {
	
	private Long id;
	private String numCartao;
	private TipoCartao tipoCartao;
	private Status status;
	private String numConta;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataVencimento;
	private BigDecimal limite;

}
