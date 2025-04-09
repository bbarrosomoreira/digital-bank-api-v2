package br.com.cdb.bancodigitaljpa.entity;

import java.math.BigDecimal;

public interface Conta {
	String getDescricaoTipoConta();
	void transferir(Conta destino, BigDecimal valor);
	BigDecimal getSaldo();
	void pix(Conta destino, BigDecimal valor);
	void depositar(BigDecimal valor);
	void sacar(BigDecimal valor);
	void setarTarifa(BigDecimal valor);
}
