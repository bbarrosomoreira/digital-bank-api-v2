package br.com.cdb.bancodigital.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import br.com.cdb.bancodigital.utils.NumberGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Conta implements ContaBase {
	
	private Long id;
	private TipoConta tipoConta;
	private String numeroConta;
	private BigDecimal saldo;
	private Moeda moeda;
	private Cliente cliente;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataCriacao;
	private BigDecimal tarifaManutencao;
	private BigDecimal saldoEmReais;
	private BigDecimal taxaRendimento;

	public Conta(Cliente cliente, TipoConta tipoConta) {
		this.tipoConta = tipoConta;
		this.cliente = cliente;
		this.dataCriacao = LocalDate.now();
		this.numeroConta = NumberGenerator.gerarNumeroConta(tipoConta);
	}

	@Override
	public void depositar(BigDecimal valor) {
		this.saldo = this.saldo.add(valor);
	}

	@Override
	public void transferir(ContaBase destino, BigDecimal valor) {
		this.sacar(valor);
		destino.depositar(valor);
	}
	
	@Override
	public void pix(ContaBase destino, BigDecimal valor) {
		this.transferir(destino, valor);
	}

	public void sacar(BigDecimal valor) {
		switch (tipoConta) {
			case CORRENTE -> {
				BigDecimal novoSaldo = this.getSaldo().subtract(valor);
				this.setSaldo(novoSaldo);
			}
			case POUPANCA -> {
				BigDecimal valorSaque = valor.add(BigDecimal.ONE);
				BigDecimal novoSaldo = this.getSaldo().subtract(valorSaque);
				this.setSaldo(novoSaldo);
			}
			case INTERNACIONAL -> {
				BigDecimal valorSaque = valor.add(BigDecimal.TWO);
				BigDecimal novoSaldo = this.getSaldo().subtract(valorSaque);
				this.setSaldo(novoSaldo);
			}
		}

	}

	// CC
	public void aplicarTarifaManutencao() {
		BigDecimal novoSaldo = this.getSaldo().subtract(tarifaManutencao);
		this.setSaldo(novoSaldo);
	}

	// CP
	public void aplicarRendimento() {
		BigDecimal rendimento = this.getSaldo().multiply(taxaRendimento).setScale(2, RoundingMode.HALF_UP);
		BigDecimal novoSaldo = this.getSaldo().add(rendimento);
		this.setSaldo(novoSaldo);
	}
	
}
