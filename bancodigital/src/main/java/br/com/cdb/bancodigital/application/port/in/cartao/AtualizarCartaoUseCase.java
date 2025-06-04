package br.com.cdb.bancodigital.application.port.in.cartao;

import br.com.cdb.bancodigital.application.core.domain.dto.response.LimiteResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.RessetarLimiteDiarioResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.StatusCartaoResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Status;

import java.math.BigDecimal;

public interface AtualizarCartaoUseCase {
    LimiteResponse alterarLimite(Long id_cartao, BigDecimal valor);
    StatusCartaoResponse alterarStatus(Long id_cartao, Usuario usuarioLogado, Status statusNovo);
    void alterarSenha(Long id_cartao, Usuario usuarioLogado, String senhaAntiga, String senhaNova);
    RessetarLimiteDiarioResponse ressetarDebito(Long id_cartao);
}
