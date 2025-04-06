//package br.com.cdb.bancodigitaljpa.dto;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
//
//public record SeguroResponse (
//		String tipoSeguro,
//		String numApolice,
//		LocalDate dataContratacao,
//		String numCartao,
//		String categoriaCliente,
//		BigDecimal valorApolice,
//		String descricaoCondicoes,
//		BigDecimal premioApolice
//		) {
//	
//	public static SeguroResponse toSeguroResponse (SeguroBase seguro) {
//		return new SeguroResponse (
//				seguro.getTipoSeguro().getNome(),
//				seguro.getNumApolice(),
//				seguro.getDataContratacao(),
//				seguro.getCartaoCredito().getNumeroCartao(),
//				seguro.getCartaoCredito().getConta().getCliente().getCategoria().getDescricao(),
//				seguro.getValorApolice(),
//				seguro.getDescricaoCondicoes(),
//				seguro.getPremioApolice()
//				);
//				
//	}
//	
//	
//	
//
//
//
//}
