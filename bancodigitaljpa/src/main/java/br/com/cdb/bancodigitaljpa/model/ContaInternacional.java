package br.com.cdb.bancodigitaljpa.model;

import java.beans.Transient;
import java.math.BigDecimal;

import br.com.cdb.bancodigitaljpa.model.enums.Moeda;
import br.com.cdb.bancodigitaljpa.model.enums.TipoConta;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ContaInternacional extends ContaBase {

    private BigDecimal tarifaManutencao;
    private BigDecimal saldoEmReais;

    public ContaInternacional(Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
        super(cliente);
        this.setMoeda(moeda);
        this.setSaldoEmReais(valorDeposito);
    }

    @Override
    @Transient
    public TipoConta getTipoConta() {
        return TipoConta.INTERNACIONAL;
    }

    @Override
    public void sacar(BigDecimal valor) {
        BigDecimal novoSaldo = this.getSaldo().subtract(valor);
        this.setSaldo(novoSaldo);
    }

    public void aplicarTxManutencao() {
        BigDecimal novoSaldo = this.getSaldo().subtract(tarifaManutencao);
        this.setSaldo(novoSaldo);
    }

    protected void gerarNumeroConta() {
        this.numeroConta = "CI-" + (10000 + (int) (Math.random() * 9000));
    }


}
