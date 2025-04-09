package br.com.cdb.bancodigitaljpa.response;

public class CpfValidationResponse {
	private String cpf;
	private Boolean valid;
	private String status;
	private String message;
	
	//G&S
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Boolean getValid() {
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
