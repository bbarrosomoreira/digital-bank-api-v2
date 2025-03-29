package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContaResponse (
		Long id_conta,
		String numConta,
		TipoConta tipoConta,
		Long id_cliente,
		Moeda moeda,
		LocalDate dataCriacao,
		// Campos específicos (opcionais)
	    BigDecimal taxaManutencao,
	    BigDecimal taxaRendimento
		){
	
    // Método estático para construção a partir de ContaBase
    public static ContaResponse fromContaBase(ContaBase conta) {
        return new ContaResponse(
            conta.getId(),
            conta.getNumeroConta(),
            conta.getTipoConta(),
            conta.getCliente().getId(),
            conta.getMoeda(),
            conta.getDataCriacao(),
            // Campos específicos
            (conta instanceof ContaCorrente cc) ? cc.getTaxaManutencao() : null,
            (conta instanceof ContaPoupanca cp) ? cp.getTaxaRendimento() : null
        );
    }
}
