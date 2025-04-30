 package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import lombok.*;

 @Getter
 @Setter
 @NoArgsConstructor
 @AllArgsConstructor
 @Builder
 @ToString
public class ContaCorrente extends ContaBase{

	private BigDecimal tarifaManutencao;

	@Override
	@Transient
	public TipoConta getTipoConta() {
		return TipoConta.CORRENTE;
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
		this.numeroConta = "CC-" + (1000 + (int) (Math.random() * 9000));
	}
	
}
