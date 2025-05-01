package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.cdb.bancodigitaljpa.model.enums.Moeda;
import br.com.cdb.bancodigitaljpa.model.enums.TipoConta;
import jakarta.validation.constraints.Digits;

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
	
	//G
	public Long getId() {
		return id;
	}
	public String getNumConta() {
		return numConta;
	}
	public TipoConta getTipoConta() {
		return tipoConta;
	}
	public Moeda getMoeda() {
		return moeda;
	}
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}
	public BigDecimal getTaxa() {
		return taxa;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public BigDecimal getSaldoEmReais() {
		return saldoEmReais;
	}
	public void setSaldoEmReais(BigDecimal saldoEmReais) {
		this.saldoEmReais = saldoEmReais;
	}
	//C
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
