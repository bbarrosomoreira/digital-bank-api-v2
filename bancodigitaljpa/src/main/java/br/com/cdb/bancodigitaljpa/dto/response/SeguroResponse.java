package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.model.Seguro;
import br.com.cdb.bancodigitaljpa.model.enums.Status;

public class SeguroResponse {
	
	private String tipoSeguro;
	private String numApolice;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataContratacao;
	private String numCartao;
	private String categoriaCliente;
	private BigDecimal valorApolice;
	private String descricaoCondicoes;
	private BigDecimal premioApolice;
	private Status statusSeguro;
	
	//C
	public SeguroResponse(String tipoSeguro, String numApolice, LocalDate dataContratacao, String numCartao, String categoriaCliente, BigDecimal valorApolice, String descricaoCondicoes, BigDecimal premioApolice, Status statusSeguro) {
		this.tipoSeguro = tipoSeguro;
		this.numApolice = numApolice;
		this.dataContratacao = dataContratacao;
		this.numCartao = numCartao;
		this.categoriaCliente = categoriaCliente;
		this.valorApolice = valorApolice;
		this.descricaoCondicoes = descricaoCondicoes;
		this.premioApolice = premioApolice;
		this.statusSeguro = statusSeguro;
	}
	
	//G
	public String getTipoSeguro() {
		return tipoSeguro;
	}
	public String getNumApolice() {
		return numApolice;
	}
	public LocalDate getDataContratacao() {
		return dataContratacao;
	}
	public String getNumCartao() {
		return numCartao;
	}
	public String getCategoriaCliente() {
		return categoriaCliente;
	}
	public BigDecimal getValorApolice() {
		return valorApolice;
	}
	public String getDescricaoCondicoes() {
		return descricaoCondicoes;
	}
	public BigDecimal getPremioApolice() {
		return premioApolice;
	}	
	public Status getStatusSeguro() {
		return statusSeguro;
	}

	public static SeguroResponse toSeguroResponse (Seguro seguro) {
		return new SeguroResponse (
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getDataContratacao(),
				seguro.getCartaoCredito().getNumeroCartao(),
				seguro.getCartaoCredito().getConta().getCliente().getCategoria().getDescricao(),
				seguro.getValorApolice(),
				seguro.getDescricaoCondicoes(),
				seguro.getPremioApolice(),
				seguro.getStatusSeguro()
				);
				
	}
	
	
	



}
