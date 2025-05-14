package br.com.cdb.bancodigital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static br.com.cdb.bancodigital.utils.ConstantUtils.*;

@Getter
@Setter
@NoArgsConstructor
public class AtualizarCategoriaClienteDTO {

	@JsonProperty(CATEGORIA)
	@NotNull(message = CATEGORIA_OBRIGATORIA)
	private CategoriaCliente categoria;
	
	public CategoriaCliente getCategoriaCliente() {
		return categoria;
	}
	
}
