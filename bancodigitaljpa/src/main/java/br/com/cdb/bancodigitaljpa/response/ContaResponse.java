package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;

public class ContaResponse {
	
	private Long id;
	private String numConta;
	private TipoConta tipoConta;
	private Long id_cliente;
	private Moeda moeda;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataCriacao;
	private BigDecimal taxa;
	
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
	//C
    public ContaResponse(Long id, String numConta, TipoConta tipoConta, 
                          Long id_cliente, Moeda moeda, LocalDate dataCriacao, BigDecimal taxa) {
        this.id = id;
        this.numConta = numConta;
        this.tipoConta = tipoConta;
        this.id_cliente = id_cliente;
        this.moeda = moeda;
        this.dataCriacao = dataCriacao;
        this.taxa = taxa;
    }
	
}
