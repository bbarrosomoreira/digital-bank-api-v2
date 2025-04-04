package br.com.cdb.bancodigitaljpa.exceptions;

public class CartaoNaoEncontradoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CartaoNaoEncontradoException(Long id) {
		super("Cartao com ID " + id + " não encontrado.");
	}

}
