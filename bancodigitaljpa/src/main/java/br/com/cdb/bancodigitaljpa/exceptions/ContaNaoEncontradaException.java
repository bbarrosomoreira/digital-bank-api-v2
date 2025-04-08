package br.com.cdb.bancodigitaljpa.exceptions;

public class ContaNaoEncontradaException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ContaNaoEncontradaException(Long id) {
		super("Conta com ID "+id+" não encontrada.");
	}
}
