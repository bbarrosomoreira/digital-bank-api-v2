package br.com.cdb.bancodigitaljpa.exceptions;

public class ClienteNaoEncontradoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ClienteNaoEncontradoException(Long id) {
		super("Cliente com ID "+id+" não encontrado.");
	}

}
