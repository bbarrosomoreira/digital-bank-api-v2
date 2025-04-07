package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigitaljpa.entity.SeguroFraude;

public record AcionarSeguroResponse  (
		String tipoSeguro,
		String numApolice,
		LocalDate dataAcionamento,
		String numCartao,
		String categoriaCliente,
		BigDecimal valorApolice,
		BigDecimal valorFraude,
		BigDecimal valorRessarcido,
		String descricaoCondicoes,
		BigDecimal premioApolice
		)
{
		
	public static AcionarSeguroResponse toSeguroResponse (SeguroFraude seguro) {
		return new AcionarSeguroResponse (
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getDataAcionamento(),
				seguro.getCartaoCredito().getNumeroCartao(),
				seguro.getCartaoCredito().getConta().getCliente().getCategoria().getDescricao(),
				seguro.getValorApolice(),
				seguro.getValorFraude(),
				seguro.getValorRessarcido(),
				seguro.getDescricaoCondicoes(),
				seguro.getPremioApolice()
				);
				
	}

}
