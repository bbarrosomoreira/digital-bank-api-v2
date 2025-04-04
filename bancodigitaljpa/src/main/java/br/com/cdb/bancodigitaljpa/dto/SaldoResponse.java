package br.com.cdb.bancodigitaljpa.dto;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;

public record SaldoResponse (
		Long id,
		String numConta,
		TipoConta tipoConta,
		Long id_cliente,
		Moeda moeda,
		BigDecimal saldo
		){
	
    // Método estático para construção a partir de ContaBase
    public static SaldoResponse fromContaBase(ContaBase conta) {
        return new SaldoResponse(
            conta.getId(),
            conta.getNumeroConta(),
            conta.getTipoConta(),
            conta.getCliente().getId(),
            conta.getMoeda(),
            conta.getSaldo()
        );
    }

}
