package br.com.cdb.bancodigitaljpa.entity;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.exceptions.SaldoInsuficienteException;

public interface Conta {
	String getTipo();
	void transferir(Conta destino, BigDecimal valor) throws SaldoInsuficienteException;
	BigDecimal getSaldo();
	void pix(Conta destino, BigDecimal valor) throws SaldoInsuficienteException;
	void depositar(BigDecimal valor);
	void sacar(BigDecimal valor) throws SaldoInsuficienteException;
}
