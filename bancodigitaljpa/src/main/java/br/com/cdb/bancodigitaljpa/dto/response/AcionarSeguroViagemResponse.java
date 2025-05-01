package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigitaljpa.model.SeguroViagem;

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
		
	public static AcionarSeguroViagemResponse toSeguroViagemResponse (SeguroViagem seguro) {
		return new AcionarSeguroViagemResponse (
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getDataAcionamento(),
				seguro.getCartaoCredito().getNumeroCartao(),
				seguro.getCartaoCredito().getConta().getCliente().getCategoria().getDescricao(),
				seguro.getValorApolice(),
				seguro.getDescricaoCondicoes(),
				seguro.getPremioApolice()
				);
				
	}

}
