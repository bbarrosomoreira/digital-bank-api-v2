package br.com.cdb.bancodigitaljpa.dto;

import br.com.cdb.bancodigitaljpa.enums.Status;

public class AlterarStatusCartaoDTO {
	
	private Status status;
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public AlterarStatusCartaoDTO() {}

}
