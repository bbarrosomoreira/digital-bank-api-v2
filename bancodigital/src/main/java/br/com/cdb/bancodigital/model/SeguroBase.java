package br.com.cdb.bancodigital.model;

import br.com.cdb.bancodigital.model.enums.Status;

public interface SeguroBase {
	void setarStatusSeguro(Status statusNovo);
	void acionarSeguro();
	void aplicarPremio();
}
