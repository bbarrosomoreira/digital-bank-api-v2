package br.com.cdb.bancodigital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AtualizarCategoriaClienteDTO {

	@JsonProperty("categoria")
	@NotNull(message = "Categoria é um campo obrigatório")
	private CategoriaCliente categoria;
	
	public CategoriaCliente getCategoriaCliente() {
		return categoria;
	}
	
}