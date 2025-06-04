package br.com.cdb.bancodigital.application.port.in.conta;

import br.com.cdb.bancodigital.application.core.domain.dto.response.ContaResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;

import java.util.List;

public interface ListarContaUseCase {
    List<ContaResponse> getContas();
    List<ContaResponse> getContaByUsuario(Usuario usuarioLogado);
    List<ContaResponse> getContaByCliente(Long id_cliente, Usuario usuarioLogado);
    ContaResponse getContaById(Long id_conta, Usuario usuarioLogado);
}
