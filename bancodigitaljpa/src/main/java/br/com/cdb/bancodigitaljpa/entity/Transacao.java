package br.com.cdb.bancodigitaljpa.entity;

import java.math.BigDecimal;
import java.time.LocalTime;

import jakarta.persistence.Entity;
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
	
	private BigDecimal valor;
	
	private LocalTime data;
	
	private String tipo; //SAQUE, DEPOSITO, etc
	
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	//constructor
	public Transacao() {
	}
	
	
	
}
