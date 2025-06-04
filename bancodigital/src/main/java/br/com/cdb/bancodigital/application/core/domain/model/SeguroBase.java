package br.com.cdb.bancodigital.application.core.domain.model;

import br.com.cdb.bancodigital.application.core.domain.model.enums.Status;

public interface SeguroBase {
	void setarStatusSeguro(Status statusNovo);
	void acionarSeguro();
	void aplicarPremio();
}
