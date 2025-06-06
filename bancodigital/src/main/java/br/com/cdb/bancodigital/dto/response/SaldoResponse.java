package br.com.cdb.bancodigital.dto.response;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;

public record SaldoResponse (
		String numConta,
		TipoConta tipoConta,
		String nomeCliente,
		Moeda moeda,
		BigDecimal saldo
		){

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
