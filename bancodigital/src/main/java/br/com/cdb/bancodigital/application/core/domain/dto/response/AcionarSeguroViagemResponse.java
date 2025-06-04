package br.com.cdb.bancodigital.application.core.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigital.application.core.domain.model.Seguro;

public record AcionarSeguroViagemResponse(
		String tipoSeguro,
		String numApolice,
		LocalDate dataAcionamento,
		String numCartao,
		String categoriaCliente,
		BigDecimal valorApolice,
		String descricaoCondicoes,
		BigDecimal premioApolice
		)
{
		
	public static AcionarSeguroViagemResponse toSeguroViagemResponse (Seguro seguro) {
		return new AcionarSeguroViagemResponse (
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getDataAcionamento(),
				seguro.getCartao().getNumeroCartao(),
				seguro.getCartao().getConta().getCliente().getCategoria().getDescricao(),
				seguro.getValorApolice(),
				seguro.getDescricaoCondicoes(),
				seguro.getPremioApolice()
				);
				
	}

}
