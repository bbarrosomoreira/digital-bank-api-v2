package br.com.cdb.bancodigitaljpa.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.enums.Moeda;
import br.com.cdb.bancodigitaljpa.enums.TipoConta;

public record SaldoResponse (
		String numConta,
		TipoConta tipoConta,
		String nomeCliente,
		Moeda moeda,
		BigDecimal saldo
		){
	
    // Método estático para construção a partir de ContaBase
    public static SaldoResponse toSaldoResponse(ContaBase conta) {
        return new SaldoResponse(
            conta.getNumeroConta(),
            conta.getTipoConta(),
            conta.getCliente().getNome(),
            conta.getMoeda(),
            conta.getSaldo()
        );
    }

}
