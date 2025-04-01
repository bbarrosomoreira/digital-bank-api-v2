package br.com.cdb.bancodigitaljpa.dto;

import java.util.Arrays;

import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class AtualizarCategoriaClienteDTO {

	@NotNull(message = "Categoria não pode ser nula")
	@Enumerated(EnumType.STRING)
	private CategoriaCliente categoria;
	
	public CategoriaCliente getCategoriaCliente() {
		return categoria;
	}
	
	public void setCategoriaCliente(String categoria) {
		try {
			
			this.categoria = CategoriaCliente.valueOf(categoria.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Categoria inválida. Use: "
					+ Arrays.toString(CategoriaCliente.values()));
		}
	}
	
	public void setCategoriaCliente(CategoriaCliente categoria) {
		this.categoria = categoria;
	}
	
	public AtualizarCategoriaClienteDTO() {}

}