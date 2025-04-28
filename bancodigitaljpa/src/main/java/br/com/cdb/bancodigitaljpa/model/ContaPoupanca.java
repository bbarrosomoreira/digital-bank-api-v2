package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
@DiscriminatorValue("POUPANCA")
public class ContaPoupanca extends ContaBase {
	
	//atributos
	@Column(name = "taxa_rendimento", precision = 5, scale = 4)
	private BigDecimal taxaRendimento;
	
	//constructor
	public ContaPoupanca() {}
	public ContaPoupanca(Cliente cliente) {
		super(cliente);
	}
	
	//getters & setters
	@Override
	public String getDescricaoTipoConta() {
		return TipoConta.POUPANCA.getDescricao();
	}
	@Override
	@Transient
	public TipoConta getTipoConta() {
		return TipoConta.POUPANCA;
	}
	public BigDecimal getTaxaRendimento() {
		return taxaRendimento;
	}
	public void setTaxaRendimento(BigDecimal taxaRendimento) {
		this.taxaRendimento = taxaRendimento;
	}
	
	//metodos	
	@PrePersist
	private void init() {
		gerarNumeroConta();
	}
	
	@Override
	public void sacar(BigDecimal valor){
		BigDecimal valorSaque = valor.add(BigDecimal.ONE);
		BigDecimal novoSaldo = this.getSaldo().subtract(valorSaque);
		this.setSaldo(novoSaldo);
	}
	
	public void aplicarRendimento() {
		BigDecimal rendimento = this.getSaldo().multiply(taxaRendimento).setScale(2, RoundingMode.HALF_UP);
		BigDecimal novoSaldo = this.getSaldo().add(rendimento);
		this.setSaldo(novoSaldo);
	}

	protected void gerarNumeroConta() {
		this.numeroConta = "CP-" + (5000 + (int)(Math.random() *9000));
	}


	

	
}
