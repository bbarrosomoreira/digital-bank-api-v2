package br.com.cdb.bancodigital.application.port.in.cartao;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

import java.util.List;

public interface ListarCartaoUseCase {
    List<CartaoResponse> getCartoes();
    List<CartaoResponse> listarPorConta(Long id_conta, Usuario usuarioLogado);
    List<CartaoResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado);
    CartaoResponse getCartaoById(Long id_cartao, Usuario usuarioLogado);
    List<CartaoResponse> listarPorUsuario(Usuario usuario);
}
