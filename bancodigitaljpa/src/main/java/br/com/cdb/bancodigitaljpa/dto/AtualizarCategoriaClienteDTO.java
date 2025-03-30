package br.com.cdb.bancodigitaljpa.dto;

import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;
import jakarta.validation.constraints.NotNull;

public class AtualizarCategoriaClienteDTO {

	@NotNull
	private CategoriaCliente categoria;
	
	public CategoriaCliente getCategoriaCliente() {
		return categoria;
	}
	public void setCategoriaCliente(String categoria) {
		this.categoria = CategoriaCliente.valueOf(categoria.toUpperCase());
	}
	public void setCategoriaCliente(TipoConta tipoConta) {
		this.categoria = CategoriaCliente.valueOf(categoria.name().toUpperCase());
	}
	
	public AtualizarCategoriaClienteDTO() {}

}