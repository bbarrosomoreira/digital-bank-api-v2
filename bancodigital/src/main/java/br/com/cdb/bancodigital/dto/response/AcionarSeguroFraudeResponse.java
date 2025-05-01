package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigital.model.Seguro;

public record AcionarSeguroFraudeResponse  (
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
		
	public static AcionarSeguroFraudeResponse toSeguroFraudeResponse (Seguro seguro) {
		return new AcionarSeguroFraudeResponse (
				seguro.getTipoSeguro().getNome(),
				seguro.getNumApolice(),
				seguro.getDataAcionamento(),
				seguro.getCartao().getNumeroCartao(),
				seguro.getCartao().getConta().getCliente().getCategoria().getDescricao(),
				seguro.getValorApolice(),
				seguro.getValorFraude(),
				seguro.getValorRessarcido(),
				seguro.getDescricaoCondicoes(),
				seguro.getPremioApolice()
				);
				
	}

}
