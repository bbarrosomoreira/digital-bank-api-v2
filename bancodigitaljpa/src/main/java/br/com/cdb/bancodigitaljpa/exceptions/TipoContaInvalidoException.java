package br.com.cdb.bancodigitaljpa.exceptions;

public class TipoContaInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TipoContaInvalidoException(String message) {
		super(message);
	}
}
