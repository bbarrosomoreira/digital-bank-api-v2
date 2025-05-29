package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cdb.bancodigital.model.Seguro;
import br.com.cdb.bancodigital.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeguroResponse {
	
	private String tipoSeguro;
	private String numApolice;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataContratacao;
	private String numCartao;
	private String categoriaCliente;
	private BigDecimal valorApolice;
	private String descricaoCondicoes;
	private BigDecimal premioApolice;
	private Status statusSeguro;

	public static SeguroResponse toSeguroResponse (Seguro seguro) {
		return new SeguroResponse (
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getDataContratacao(),
				seguro.getCartao().getNumeroCartao(),
				seguro.getCartao().getConta().getCliente().getCategoria().getDescricao(),
				seguro.getValorApolice(),
				seguro.getDescricaoCondicoes(),
				seguro.getPremioApolice(),
				seguro.getStatusSeguro()
				);
				
	}
	
	
	



}
