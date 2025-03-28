package br.com.cdb.bancodigitaljpa.exceptions;

public class CpfExistenteException  extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CpfExistenteException(String cpf) {
		super("CPF " + cpf + " já está cadastrado.");
	}
}
