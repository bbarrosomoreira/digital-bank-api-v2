package br.com.cdb.bancodigitaljpa.dto;

import br.com.cdb.bancodigitaljpa.entity.ContaBase;
import br.com.cdb.bancodigitaljpa.entity.ContaCorrente;
import br.com.cdb.bancodigitaljpa.entity.ContaPoupanca;

public class ContaResponseFactory {
    public static ContaResponse createFromConta(ContaBase conta) {
        if (conta instanceof ContaCorrente cc) {
            return new ContaCorrenteResponse(
                cc.getId(),
                cc.getNumeroConta(),
                cc.getCliente().getId(),
                cc.getMoeda(),
                cc.getDataCriacao(),
                cc.getTarifaManutencao()
            );
        } else if (conta instanceof ContaPoupanca cp) {
            return new ContaPoupancaResponse(
                cp.getId(),
                cp.getNumeroConta(),
                cp.getCliente().getId(),
                cp.getMoeda(),
                cp.getDataCriacao(),
                cp.getTaxaRendimento()
            );
        }
        throw new IllegalArgumentException("Tipo de conta não suportado: " + conta.getClass().getSimpleName());
    }

}
