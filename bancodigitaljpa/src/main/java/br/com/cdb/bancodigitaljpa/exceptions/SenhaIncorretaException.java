package br.com.cdb.bancodigitaljpa.exceptions;

public class SenhaIncorretaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SenhaIncorretaException(String menssagem) {
		super(menssagem);
	}

}
