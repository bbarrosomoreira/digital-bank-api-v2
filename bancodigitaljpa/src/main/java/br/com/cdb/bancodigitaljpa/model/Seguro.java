package br.com.cdb.bancodigitaljpa.model;

import br.com.cdb.bancodigitaljpa.model.enums.Status;

public interface Seguro {
	void setarStatusSeguro(Status statusNovo);
	void acionarSeguro();
	void aplicarPremio();
}
