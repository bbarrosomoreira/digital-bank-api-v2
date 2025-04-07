package br.com.cdb.bancodigitaljpa.entity;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import br.com.cdb.bancodigitaljpa.exceptions.SaldoInsuficienteException;
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
	
	//getters & setters
	@Override
	public String getTipo() {
		return TipoConta.POUPANCA.getDescricao();
	}
	public BigDecimal getTaxaRendimento() {
		return taxaRendimento;
	}
	public void setTaxaRendimento(BigDecimal taxaRendimento) {
		this.taxaRendimento = taxaRendimento;
	}
	public void setarTarifa(BigDecimal taxaRendimento) {
		this.setTaxaRendimento(taxaRendimento);
	}
	
	//constructor
	public ContaPoupanca() {}
	
	public ContaPoupanca(Cliente cliente) {
		super(cliente);
	}
	
	//metodos
	@Override
	@Transient
	public TipoConta getTipoConta() {
		return TipoConta.POUPANCA;
	}
	
	@PrePersist
	private void init() {
		gerarNumeroConta();
	}
	
	@Override
	public void sacar(BigDecimal valor) throws SaldoInsuficienteException {
		if (valor.compareTo(this.getSaldo()) > 0) {
			throw new SaldoInsuficienteException(this.getId(), this.numeroConta, this.getSaldo());
		}
		BigDecimal novoSaldo = this.getSaldo().subtract(valor);
		this.setSaldo(novoSaldo);
	}
	
	public void aplicarRendimento() {
		BigDecimal rendimento = this.getSaldo().multiply(taxaRendimento);
		BigDecimal novoSaldo = this.getSaldo().add(rendimento);
		this.setSaldo(novoSaldo);
	}

	protected void gerarNumeroConta() {
		this.numeroConta = "CP-" + (5000 + (int)(Math.random() *9000));
	}


	

	
}
