package br.com.cdb.bancodigital.model;

import java.math.BigDecimal;

public interface ContaBase {
	void depositar(BigDecimal valor);
	void transferir(ContaBase destino, BigDecimal valor);
	void pix(ContaBase destino, BigDecimal valor);
	void sacar(BigDecimal valor);
}
