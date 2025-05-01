package br.com.cdb.bancodigital.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EnderecoCliente {

	@NotBlank(message = "CEP é obrigatório")
	@Pattern(regexp = "\\d{8}", message = "CEP deve estar no formato XXXXXXXX")
	private String cep;

	@NotBlank(message = "Rua é um campo obrigatório")
	@Size(max = 100)
	private String rua;

	@NotNull(message = "Numero é um campo obrigatório - Se endereço não tiver número, digite 0")
	private int numero;

	@Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
	private String complemento;

	@NotBlank(message = "Bairro é um campo obrigatório")
	@Size(max = 100)
	private String bairro;

	@NotBlank(message = "Cidade é obrigatória")
    @Size(max = 50, message = "Cidade deve ter no máximo 50 caracteres")
	private String cidade;

	@NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (ex: SP)")
	private String estado;

	private boolean enderecoPrincipal;


}
