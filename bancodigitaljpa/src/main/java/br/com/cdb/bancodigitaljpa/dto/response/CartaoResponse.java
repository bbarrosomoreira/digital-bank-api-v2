package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.model.enums.Status;
import br.com.cdb.bancodigitaljpa.model.enums.TipoCartao;

public class CartaoResponse {
	
	private Long id;
	private String numCartao;
	private TipoCartao tipoCartao;
	private Status status;
	private String numConta;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataVencimento;
	private BigDecimal limite;
	
	//G
	public Long getId() {
		return id;
	}
	public String getNumCartao() {
		return numCartao;
	}
	public TipoCartao getTipoCartao() {
		return tipoCartao;
	}
	public Status getStatus() {
		return status;
	}
	public String getNumConta() {
		return numConta;
	}
	public LocalDate getDataVencimento() {
		return dataVencimento;
	}
	public BigDecimal getLimite() {
		return limite;
	}

	//C
	public CartaoResponse(Long id, String numCartao, TipoCartao tipoCartao, Status status, String numConta,
			LocalDate dataVencimento, BigDecimal limite) {
		this.id = id;
		this.numCartao = numCartao;
		this.tipoCartao = tipoCartao;
		this.status = status;
		this.numConta = numConta;
		this.dataVencimento = dataVencimento;
		this.limite = limite;
	}
	
	
	
	
	
	
	

}
