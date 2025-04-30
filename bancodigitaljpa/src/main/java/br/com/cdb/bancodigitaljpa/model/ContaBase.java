package br.com.cdb.bancodigitaljpa.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public abstract class ContaBase implements Conta {
	
	private Long id;
	protected String numeroConta;
	private BigDecimal saldo = BigDecimal.ZERO;
	private Moeda moeda;
	private Cliente cliente;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataCriacao = LocalDate.now();

	public abstract TipoConta getTipoConta();

	protected ContaBase(Cliente cliente) {
		this.cliente = cliente;
		gerarNumeroConta();
	}

	//M 
	@Override
	public void depositar(BigDecimal valor) {
		this.saldo = this.saldo.add(valor);
	}

	@Override
	public void transferir(Conta destino, BigDecimal valor) {
		this.sacar(valor);
		destino.depositar(valor);
	}
	
	@Override
	public void pix(Conta destino, BigDecimal valor) {
		this.transferir(destino, valor);
	}
	
	protected abstract void gerarNumeroConta();
	
}
