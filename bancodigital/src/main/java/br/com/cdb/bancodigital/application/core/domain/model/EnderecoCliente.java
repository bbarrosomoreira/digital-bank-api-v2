package br.com.cdb.bancodigital.application.core.domain.model;

import br.com.cdb.bancodigital.utils.ConstantUtils;
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

	private Long id;
	private Cliente cliente;

	@NotBlank(message = ConstantUtils.CEP_OBRIGATORIO)
	@Pattern(regexp = "\\d{8}", message = ConstantUtils.CEP_FORMATO)
	private String cep;

	@NotBlank(message = ConstantUtils.RUA_OBRIGATORIA)
	@Size(max = 100)
	private String rua;

	@NotNull(message = ConstantUtils.NUMERO_OBRIGATORIO)
	private int numero;

	@Size(max = 100, message = ConstantUtils.COMPLEMENTO_TAMANHO)
	private String complemento;

	@NotBlank(message = ConstantUtils.BAIRRO_OBRIGATORIO)
	@Size(max = 100)
	private String bairro;

	@NotBlank(message = ConstantUtils.CIDADE_OBRIGATORIA)
	@Size(max = 100, message = ConstantUtils.CIDADE_TAMANHO)
	private String cidade;

	@NotBlank(message = ConstantUtils.ESTADO_OBRIGATORIO)
	@Size(min = 2, max = 2, message = ConstantUtils.ESTADO_TAMANHO)
	private String estado;

}