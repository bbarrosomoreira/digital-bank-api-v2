package br.com.cdb.bancodigital.application.port.in.conta;

import br.com.cdb.bancodigital.application.core.domain.dto.response.*;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;

import java.math.BigDecimal;

public interface TransacoesUseCase {
    TransferenciaResponse transferir(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor);
    PixResponse pix(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor);
    SaldoResponse getSaldo(Long id_conta, Usuario usuarioLogado);
    DepositoResponse depositar(Long id_conta, BigDecimal valor);
    SaqueResponse sacar(Long id_conta, Usuario usuarioLogado, BigDecimal valor);
    AplicarTxManutencaoResponse debitarTarifaManutencao(Long id_conta);
    AplicarTxRendimentoResponse creditarRendimento(Long id_conta);
}
