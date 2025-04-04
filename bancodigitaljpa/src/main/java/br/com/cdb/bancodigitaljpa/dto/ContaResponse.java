package br.com.cdb.bancodigitaljpa.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;

public abstract class ContaResponse {
	protected Long id;
	protected String numConta;
	protected TipoConta tipoConta;
	protected Long id_cliente;
	protected Moeda moeda;
	@JsonFormat(pattern = "dd-MM-yyyy")
	protected LocalDate dataCriacao;
	
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
	
	//C
    protected ContaResponse(Long id, String numConta, TipoConta tipoConta, 
                          Long id_cliente, Moeda moeda, LocalDate dataCriacao) {
        this.id = id;
        this.numConta = numConta;
        this.tipoConta = tipoConta;
        this.id_cliente = id_cliente;
        this.moeda = moeda;
        this.dataCriacao = dataCriacao;
    }
	
}
