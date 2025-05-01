package br.com.cdb.bancodigitaljpa.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.cdb.bancodigitaljpa.model.enums.CategoriaCliente;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class AtualizarCategoriaClienteDTO {

	@JsonProperty("categoria")
	@NotNull(message = "Categoria é um campo obrigatório")
	@Enumerated(EnumType.STRING)
	private CategoriaCliente categoria;
	
	public CategoriaCliente getCategoriaCliente() {
		return categoria;
	}
	
	public void setCategoriaCliente(String categoria) {
		try {
			
			this.categoria = CategoriaCliente.valueOf(categoria.toUpperCase());
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new IllegalArgumentException("Categoria inválida. Valores permitidos: "
					+ Arrays.toString(CategoriaCliente.values()));
		}
	}
	
	public void setCategoriaCliente(CategoriaCliente categoria) {
		this.categoria = categoria;
	}
	
	public AtualizarCategoriaClienteDTO() {}

}