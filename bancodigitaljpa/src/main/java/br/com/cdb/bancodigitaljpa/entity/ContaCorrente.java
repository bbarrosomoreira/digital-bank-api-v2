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
@DiscriminatorValue("CORRENTE")
public class ContaCorrente extends ContaBase{

	//atributos
	@Column(name = "taxa_manutencao", precision = 19, scale = 2)
	private BigDecimal taxaManutencao;
	
//	@Column(name = "limite_cheque_especial", precision = 19, scale = 2)
//	private BigDecimal limiteChequeEspecial;
//	
//	@Column(name = "cheque_especial_utilizado", precision = 19, scale = 2)
//	private BigDecimal chequeEspecialUtilizado = BigDecimal.ZERO;
//	
//	@Column(name = "data_uso_cheque_especial")
//	private LocalDate dataUsoCheque;
//	
//	@Column(name = "taxa_juros_cheque_especial", precision = 5, scale = 4)
//	private BigDecimal taxaJurosCheque;
	
	// construtor
	public ContaCorrente() {}
	
	public ContaCorrente(Cliente cliente) {
		super(cliente);
	}
	
	//getters & setters
	@Override
	public String getTipo() {
		return TipoConta.CORRENTE.getDescricao();
	}
	
	public BigDecimal getTaxaManutencao() {
		return taxaManutencao;
	}
	public void setTaxaManutencao(BigDecimal taxaManutencao) {
		this.taxaManutencao = taxaManutencao;
	}
	
//	public BigDecimal getLimiteChequeEspecial() {
//	return limiteChequeEspecial;
//}
//public void setLimiteChequeEspecial(BigDecimal limiteChequeEspecial) {
//	this.limiteChequeEspecial = limiteChequeEspecial;
//}
//public BigDecimal getChequeEspecialUtilizado() {
//	return chequeEspecialUtilizado;
//}
//public void setChequeEspecialUtilizado(BigDecimal chequeEspecialUtilizado) {
//	this.chequeEspecialUtilizado = chequeEspecialUtilizado;
//}
//public LocalDate getDataUsoCheque() {
//	return dataUsoCheque;
//}
//public void setDataUsoCheque(LocalDate dataUsoCheque) {
//	this.dataUsoCheque = dataUsoCheque;
//}
//public BigDecimal getTaxaJurosCheque() {
//	return taxaJurosCheque;
//}
//public void setTaxaJurosCheque(BigDecimal taxaJurosCheque) {
//	this.taxaJurosCheque = taxaJurosCheque;
//}
	
	//metodos
	@Override
	@Transient
	public TipoConta getTipoConta() {
		return TipoConta.CORRENTE;
	}
	
	@PrePersist
	private void init() {
		gerarNumeroConta();
	}
	
	@Override
	public void sacar(BigDecimal valor) throws SaldoInsuficienteException {
		if (valor.compareTo(this.getSaldo()) > 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente para saque.");
		}
		BigDecimal novoSaldo = this.getSaldo().subtract(valor);
		this.setSaldo(novoSaldo);
	}
	
	public void aplicarTxManutencao() {
		BigDecimal novoSaldo = this.getSaldo().subtract(taxaManutencao);
		this.setSaldo(novoSaldo);
	}
	
	protected void gerarNumeroConta() {
		this.numeroConta = "CC-" + (1000 + (int)(Math.random() *9000));
	}
	
}
