package br.com.cdb.bancodigital.application.port.in.seguro;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CancelarSeguroResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

public interface DeletarSeguroUseCase {
    CancelarSeguroResponse cancelarSeguro(Long id_seguro, Usuario usuarioLogado);
    void deleteSegurosByCliente(Long id_cliente);
}
