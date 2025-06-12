package br.com.cdb.bancodigital.application.port.in.cliente;

import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

public interface ListarClienteUseCase {
    List<Cliente> getClientes() throws AccessDeniedException;
    Cliente getClientePorId(Long id, Usuario usuario) throws AccessDeniedException;
    Cliente getClientePorUsuario(Usuario usuario) throws AccessDeniedException;
}
