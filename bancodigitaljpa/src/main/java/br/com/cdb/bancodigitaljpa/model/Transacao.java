package br.com.cdb.bancodigitaljpa.model;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.TipoTransacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private ContaBase contaOrigem;
	
	@ManyToOne
	private ContaBase contaDestino;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal valor;
	
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalTime data;
	
	@Enumerated(EnumType.STRING)
	private TipoTransacao tipo; //SAQUE, DEPOSITO, etc
	
	private String descricao;
	
	//GETTERS & SETTERS
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ContaBase getContaOrigem() {
		return contaOrigem;
	}
	public void setContaOrigem(ContaBase contaOrigem) {
		this.contaOrigem = contaOrigem;
	}
	public ContaBase getContaDestino() {
		return contaDestino;
	}
	public void setContaDestino(ContaBase contaDestino) {
		this.contaDestino = contaDestino;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public LocalTime getData() {
		return data;
	}
	public void setData(LocalTime data) {
		this.data = data;
	}
	public TipoTransacao getTipo() {
		return tipo;
	}
	public void setTipo(TipoTransacao tipo) {
		this.tipo = tipo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	//constructor
	public Transacao() {
	}
	
	
	
}
