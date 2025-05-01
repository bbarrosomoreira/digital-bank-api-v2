package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.model.enums.Status;

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
