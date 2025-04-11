package br.com.cdb.bancodigitaljpa.entity;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
@DiscriminatorValue("INTERNACIONAL")
public class ContaInternacional extends ContaBase {
	//atributos
		@Column(name = "tarifa_manutencao", precision = 19, scale = 2)
		private BigDecimal tarifaManutencao;
		
		@Column(precision = 19, scale = 2)
		private BigDecimal saldoEmReais;

		// construtor
		public ContaInternacional() {}
		public ContaInternacional(Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
			super(cliente);
			this.setMoeda(moeda);
			this.setSaldoEmReais(valorDeposito);
		}
		
		//getters & setters
		@Override
		public String getDescricaoTipoConta() {
			return TipoConta.INTERNACIONAL.getDescricao();
		}
		@Override
		@Transient
		public TipoConta getTipoConta() {
			return TipoConta.INTERNACIONAL;
		}
		public BigDecimal getTarifaManutencao() {
			return tarifaManutencao;
		}
		public void setTarifaManutencao(BigDecimal tarifaManutencao) {
			this.tarifaManutencao = tarifaManutencao;
		}
		public BigDecimal getSaldoEmReais() {
			return saldoEmReais;
		}
		public void setSaldoEmReais(BigDecimal saldoEmReais) {
			this.saldoEmReais = saldoEmReais;
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
			this.numeroConta = "CI-" + (10000 + (int)(Math.random() *9000));
		}
		

}
