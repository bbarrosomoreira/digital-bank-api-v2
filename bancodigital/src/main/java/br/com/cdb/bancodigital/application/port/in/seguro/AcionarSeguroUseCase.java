package br.com.cdb.bancodigital.application.port.in.seguro;

import br.com.cdb.bancodigital.application.core.domain.dto.response.DebitarPremioSeguroResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Seguro;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

import java.math.BigDecimal;

public interface AcionarSeguroUseCase {
    Seguro acionarSeguro(Long id_seguro, Usuario usuarioLogado, BigDecimal valor);
    DebitarPremioSeguroResponse debitarPremioSeguro(Long id_seguro);
}
