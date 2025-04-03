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
	@Column(name = "tarifa_manutencao", precision = 19, scale = 2)
	private BigDecimal tarifaManutencao;
	
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
	
	// virá dos parâmetros
	public BigDecimal getTarifaManutencao() {
		return tarifaManutencao;
	}
	public void setTarifaManutencao(BigDecimal tarifaManutencao) {
		this.tarifaManutencao = tarifaManutencao;
	}
	
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
			throw new SaldoInsuficienteException(this.getId(), this.numeroConta, this.getSaldo());
		}
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
