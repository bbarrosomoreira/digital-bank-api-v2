 package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
@DiscriminatorValue("CORRENTE")
public class ContaCorrente extends ContaBase{

	//atributos
	@Column(name = "tarifa_manutencao", precision = 19, scale = 2)
	private BigDecimal tarifaManutencao;
	
	// construtor
	public ContaCorrente() {}
	public ContaCorrente(Cliente cliente) {
		super(cliente);
	}
	
	//getters & setters
	@Override
	public String getDescricaoTipoConta() {
		return TipoConta.CORRENTE.getDescricao();
	}
	@Override
	@Transient
	public TipoConta getTipoConta() {
		return TipoConta.CORRENTE;
	}
	public BigDecimal getTarifaManutencao() {
		return tarifaManutencao;
	}
	public void setTarifaManutencao(BigDecimal tarifaManutencao) {
		this.tarifaManutencao = tarifaManutencao;
	}
	
	//metodos
	@PrePersist
	private void init() {
		gerarNumeroConta();
	}
	@Override
	public void sacar(BigDecimal valor) {
		BigDecimal novoSaldo = this.getSaldo().subtract(valor);
		this.setSaldo(novoSaldo);
	}
	
	public void aplicarTxManutencao() {
		BigDecimal novoSaldo = this.getSaldo().subtract(tarifaManutencao);
		this.setSaldo(novoSaldo);
	}
	
	protected void gerarNumeroConta() {
		this.numeroConta = "CC-" + (1000 + (int)(Math.random() *9000));
	}
	
}
