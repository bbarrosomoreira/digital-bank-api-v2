package br.com.cdb.bancodigitaljpa.exceptions;

public class SaldoInsuficienteException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public SaldoInsuficienteException(String mensagem) {
		super(mensagem);
	}
	
	public SaldoInsuficienteException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

}
