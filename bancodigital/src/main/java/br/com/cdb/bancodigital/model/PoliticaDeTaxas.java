package br.com.cdb.bancodigital.model;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PoliticaDeTaxas {

	private Long id;
	private CategoriaCliente categoria;

	private BigDecimal tarifaManutencaoMensalContaCorrente;
	private BigDecimal rendimentoPercentualMensalContaPoupanca;
	private BigDecimal tarifaManutencaoContaInternacional;
	private BigDecimal limiteCartaoCredito;
	private BigDecimal limiteDiarioDebito;
	private BigDecimal tarifaSeguroViagem;
	private BigDecimal tarifaSeguroFraude;
	private BigDecimal valorApoliceFraude;
	private BigDecimal valorApoliceViagem;

}
