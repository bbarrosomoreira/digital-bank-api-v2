package br.com.cdb.bancodigital.application.core.domain.entity;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Status;

public interface SeguroBase {
	void setarStatusSeguro(Status statusNovo);
	void acionarSeguro();
	void aplicarPremio();
}
