package br.com.cdb.bancodigitaljpa.exceptions;

public class CepNaoEncontradoException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public CepNaoEncontradoException(String message ) {
		super(message);
	}
}
