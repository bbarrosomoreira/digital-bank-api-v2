package br.com.cdb.bancodigital.application.port.in;

import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;

public interface SecurityUseCase {
    boolean isAdmin(Usuario usuario);
    boolean isOwner(Usuario usuario, Cliente cliente);
    void validateAccess(Usuario usuario, Cliente cliente);
}
