package br.com.cdb.bancodigital.application.port.in.cliente;

import br.com.cdb.bancodigital.application.core.domain.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

public interface ListarClienteUseCase {
    List<ClienteResponse> getClientes() throws AccessDeniedException;
    ClienteResponse getClientePorId(Long id, Usuario usuario) throws AccessDeniedException;
    ClienteResponse getClientePorUsuario(Usuario usuario) throws AccessDeniedException;
}
