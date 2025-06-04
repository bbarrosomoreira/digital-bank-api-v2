package br.com.cdb.bancodigital.application.core.domain.model;

import java.math.BigDecimal;

public interface ContaBase {
	void depositar(BigDecimal valor);
	void transferir(ContaBase destino, BigDecimal valor);
	void pix(ContaBase destino, BigDecimal valor);
	void sacar(BigDecimal valor);
}
