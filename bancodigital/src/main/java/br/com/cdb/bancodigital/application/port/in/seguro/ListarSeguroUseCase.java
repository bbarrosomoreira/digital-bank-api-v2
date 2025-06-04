package br.com.cdb.bancodigital.application.port.in.seguro;

import br.com.cdb.bancodigital.application.core.domain.dto.response.SeguroResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;

import java.util.List;

public interface ListarSeguroUseCase {
    SeguroResponse getSeguroById(Long id_seguro, Usuario usuarioLogado);
    List<SeguroResponse> listarPorUsuario(Usuario usuario);
    List<SeguroResponse> getSeguros();
    List<SeguroResponse> getSeguroByCartaoId(Long id_cartao, Usuario usuarioLogado);
    List<SeguroResponse> getSeguroByClienteId(Long id_cliente, Usuario usuarioLogado);
}
