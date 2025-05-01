package br.com.cdb.bancodigitaljpa.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.Conta;
import br.com.cdb.bancodigitaljpa.model.enums.Moeda;
import br.com.cdb.bancodigitaljpa.model.enums.TipoConta;

public record SaldoResponse (
		String numConta,
		TipoConta tipoConta,
		String nomeCliente,
		Moeda moeda,
		BigDecimal saldo
		){
	
    // Método estático para construção a partir de ContaBase
    public static SaldoResponse toSaldoResponse(Conta conta) {
        return new SaldoResponse(
            conta.getNumeroConta(),
            conta.getTipoConta(),
            conta.getCliente().getNome(),
            conta.getMoeda(),
            conta.getSaldo()
        );
    }

}
