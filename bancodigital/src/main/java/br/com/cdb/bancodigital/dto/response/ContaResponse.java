package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContaResponse {
	
	private Long id;
	private String numConta;
	private TipoConta tipoConta;
	private Moeda moeda;
	private BigDecimal saldo;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataCriacao;
	private BigDecimal taxa;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Digits(integer = 19, fraction = 2)
	private BigDecimal saldoEmReais;

    public ContaResponse(Long id, String numConta, TipoConta tipoConta, 
                    Moeda moeda, BigDecimal saldo, LocalDate dataCriacao, BigDecimal taxa) {
        this.id = id;
        this.numConta = numConta;
        this.tipoConta = tipoConta;
        this.moeda = moeda;
        this.saldo = saldo;
        this.dataCriacao = dataCriacao;
        this.taxa = taxa;
    }
	
}
