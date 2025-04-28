package br.com.cdb.bancodigitaljpa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.Status;
import br.com.cdb.bancodigitaljpa.enums.TipoSeguro;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_seguro", discriminatorType = DiscriminatorType.STRING)
public abstract class SeguroBase implements Seguro {
	
	//A
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_seguro;
	
	@Column(unique = true, nullable = false, updatable = false)
	private String numApolice;
	
	@ManyToOne
	@JoinColumn(name = "cartaoCreditoId", nullable = false, updatable = false)
	private CartaoCredito cartaoCredito;
	
	@Column(name = "data_contratacao", nullable = false, updatable = false)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataContratacao;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal valorApolice;
	
	@Size(max=300, message="A descrição deve ter no máximo 300 caracteres.")
	private String descricaoCondicoes;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal premioApolice;

	//G&S
	public Long getId() {
		return id_seguro;
	}
	public String getNumApolice() {
		return numApolice;
	}
	public CartaoCredito getCartaoCredito() {
		return cartaoCredito;
	}
	public LocalDate getDataContratacao() {
		return dataContratacao;
	}
	public BigDecimal getValorApolice() {
		return valorApolice;
	}
	public void setValorApolice(BigDecimal valorApolice) {
		this.valorApolice = valorApolice;
	}
	public String getDescricaoCondicoes() {
		return descricaoCondicoes;
	}
	public void setDescricaoCondicoes(String descricaoCondicoes) {
		this.descricaoCondicoes = descricaoCondicoes;
	}
	public BigDecimal getPremioApolice() {
		return premioApolice;
	}
	public void setPremioApolice(BigDecimal premioApolice) {
		this.premioApolice = premioApolice;
	}

	//C
	public SeguroBase(CartaoCredito ccr) {
		super();
		this.cartaoCredito = ccr;
		this.dataContratacao = LocalDate.now();
		this.numApolice = gerarNumeroApolice();
	}
	public SeguroBase() {}

	//M
	private String gerarNumeroApolice() {
		return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}
	public abstract TipoSeguro getTipoSeguro();
	public abstract Status getStatusSeguro();
	
}
