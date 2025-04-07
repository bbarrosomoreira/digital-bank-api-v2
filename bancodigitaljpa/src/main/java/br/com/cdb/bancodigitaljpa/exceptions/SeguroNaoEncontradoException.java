package br.com.cdb.bancodigitaljpa.exceptions;

public class SeguroNaoEncontradoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public SeguroNaoEncontradoException(Long id) {
		super("Seguro com ID " + id + " não encontrado.");
	}

}
