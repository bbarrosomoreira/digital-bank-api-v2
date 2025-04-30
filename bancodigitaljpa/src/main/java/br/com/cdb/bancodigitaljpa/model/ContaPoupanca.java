package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ContaPoupanca extends ContaBase {
	
	private BigDecimal taxaRendimento;

	@Override
	@Transient
	public TipoConta getTipoConta() {
		return TipoConta.POUPANCA;
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
