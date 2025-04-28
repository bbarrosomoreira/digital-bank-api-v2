package br.com.cdb.bancodigitaljpa.model;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PoliticaDeTaxas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(unique = true)
	private CategoriaCliente categoria;

	private BigDecimal tarifaManutencaoMensalContaCorrente;
	private BigDecimal tarifaManutencaoContaInternacional;

	@Column(precision = 5, scale = 4)
	private BigDecimal rendimentoPercentualMensalContaPoupanca;
	private BigDecimal limiteCartaoCredito;
	private BigDecimal limiteDiarioDebito;
	private BigDecimal tarifaSeguroViagem;
	private BigDecimal tarifaSeguroFraude;

	// G&S
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CategoriaCliente getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaCliente categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getTarifaManutencaoMensalContaCorrente() {
		return tarifaManutencaoMensalContaCorrente;
	}

	public void setTarifaManutencaoMensalContaCorrente(BigDecimal tarifaManutencaoMensalContaCorrente) {
		this.tarifaManutencaoMensalContaCorrente = tarifaManutencaoMensalContaCorrente;
	}

	public BigDecimal getRendimentoPercentualMensalContaPoupanca() {
		return rendimentoPercentualMensalContaPoupanca;
	}

	public void setRendimentoPercentualMensalContaPoupanca(BigDecimal rendimentoPercentualMensalContaPoupanca) {
		this.rendimentoPercentualMensalContaPoupanca = rendimentoPercentualMensalContaPoupanca;
	}

	public BigDecimal getLimiteCartaoCredito() {
		return limiteCartaoCredito;
	}

	public void setLimiteCartaoCredito(BigDecimal limiteCartaoCredito) {
		this.limiteCartaoCredito = limiteCartaoCredito;
	}

	public BigDecimal getLimiteDiarioDebito() {
		return limiteDiarioDebito;
	}

	public void setLimiteDiarioDebito(BigDecimal limiteDiarioDebito) {
		this.limiteDiarioDebito = limiteDiarioDebito;
	}

	public BigDecimal getTarifaSeguroViagem() {
		return tarifaSeguroViagem;
	}

	public void setTarifaSeguroViagem(BigDecimal tarifaSeguroViagem) {
		this.tarifaSeguroViagem = tarifaSeguroViagem;
	}

	public BigDecimal getTarifaSeguroFraude() {
		return tarifaSeguroFraude;
	}

	public void setTarifaSeguroFraude(BigDecimal tarifaSeguroFraude) {
		this.tarifaSeguroFraude = tarifaSeguroFraude;
	}

	public BigDecimal getTarifaManutencaoContaInternacional() {
		return tarifaManutencaoContaInternacional;
	}

	public void setTarifaManutencaoContaInternacional(BigDecimal tarifaManutencaoContaInternacional) {
		this.tarifaManutencaoContaInternacional = tarifaManutencaoContaInternacional;
	}

	// C
	public PoliticaDeTaxas() {
	}

	public PoliticaDeTaxas(CategoriaCliente categoria, BigDecimal tarifaManutencaoMensalContaCorrente,
			BigDecimal rendimentoPercentualMensalContaPoupanca, BigDecimal tarifaManutencaoContaInternacional, BigDecimal limiteCartaoCredito,
			BigDecimal limiteDiarioDebito, BigDecimal tarifaSeguroViagem, BigDecimal tarifaSeguroFraude) {
		super();
		this.categoria = categoria;
		this.tarifaManutencaoMensalContaCorrente = tarifaManutencaoMensalContaCorrente;
		this.rendimentoPercentualMensalContaPoupanca = rendimentoPercentualMensalContaPoupanca;
		this.tarifaManutencaoContaInternacional = tarifaManutencaoContaInternacional;
		this.limiteCartaoCredito = limiteCartaoCredito;
		this.limiteDiarioDebito = limiteDiarioDebito;
		this.tarifaSeguroViagem = tarifaSeguroViagem;
		this.tarifaSeguroFraude = tarifaSeguroFraude;
	}

}
