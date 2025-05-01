package br.com.cdb.bancodigital.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CpfValidationResponse {
	private Boolean success;
	private String cpf;
	private Boolean valid;
	private String status;
	private String message;
	
	//G&S
	public String getCpf() {
		return cpf;
	}
	public Boolean isSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Boolean isValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	//C
	public CpfValidationResponse () {}


}
