package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import jakarta.validation.constraints.Digits;

public class ContaResponse {
	
	private Long id;
	private String numConta;
	private TipoConta tipoConta;
	private Long id_cliente;
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
	public Long getId_cliente() {
		return id_cliente;
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
                          Long id_cliente, Moeda moeda, BigDecimal saldo, LocalDate dataCriacao, BigDecimal taxa) {
        this.id = id;
        this.numConta = numConta;
        this.tipoConta = tipoConta;
        this.id_cliente = id_cliente;
        this.moeda = moeda;
        this.saldo = saldo;
        this.dataCriacao = dataCriacao;
        this.taxa = taxa;
    }
	
}
