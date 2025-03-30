package br.com.cdb.bancodigitaljpa.entity;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.exceptions.SaldoInsuficienteException;

public interface Conta {
	BigDecimal getSaldo();
	void depositar(BigDecimal valor);
	void sacar(BigDecimal valor) throws SaldoInsuficienteException;
	String getTipo();
	void transferir(Conta destino, BigDecimal valor) throws SaldoInsuficienteException;
	void pix(Conta destino, BigDecimal valor) throws SaldoInsuficienteException;
}
