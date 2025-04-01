package br.com.cdb.bancodigitaljpa.event;

import br.com.cdb.bancodigitaljpa.entity.Cliente;

public class CategoriaClienteAtualizadaEvent {
	private final Cliente cliente;
	
	public CategoriaClienteAtualizadaEvent(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	

}
