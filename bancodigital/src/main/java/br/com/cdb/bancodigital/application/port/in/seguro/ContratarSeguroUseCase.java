package br.com.cdb.bancodigital.application.port.in.seguro;

import br.com.cdb.bancodigital.application.core.domain.dto.response.SeguroResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoSeguro;

public interface ContratarSeguroUseCase {
    SeguroResponse contratarSeguro(Long id_cartao, Usuario usuarioLogado, TipoSeguro tipo);
}
