package br.com.cdb.bancodigitaljpa.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING)
public abstract class ContaBase implements Conta {
	
	//atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_conta;
	
	@Column(unique = true)
	protected String numeroConta;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal saldo = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 3)
	private Moeda moeda;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente", nullable = false, updatable = false)
	private Cliente cliente;
	
	@Column(name = "data_criacao", nullable = false, updatable = false)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataCriacao = LocalDate.now();

	public abstract TipoConta getTipoConta();
	
	// Construtor
	protected ContaBase() {}
	
	protected ContaBase(Cliente cliente) {
		this.cliente = cliente;
		this.moeda = Moeda.BRL;
	}
	
	//Getters & Setters
	public Long getId() {
		return id_conta;
	}
	public String getNumeroConta() {
		return numeroConta;
	}
	@Override
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public Moeda getMoeda() {
		return moeda;
	}
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	//M 
	@Override
	public void depositar(BigDecimal valor) {
		this.saldo = this.saldo.add(valor);
	}

	@Override
	public void transferir(Conta destino, BigDecimal valor) {
		this.sacar(valor);
		destino.depositar(valor);
	}
	
	@Override
	public void pix(Conta destino, BigDecimal valor) {
		this.transferir(destino, valor);
	}
	
	protected abstract void gerarNumeroConta();
	
}
