package br.com.cdb.bancodigital.application.port.in.cartao;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoCartao;

public interface EmitirCartaoUseCase {
    CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha);
}
