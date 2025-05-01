package br.com.cdb.bancodigital.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.model.Cliente;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.model.enums.Role;

@Service
public class SecurityService {

    public boolean isAdmin(Usuario usuario) {
        return usuario.getRole() == Role.ADMIN;
    }

    public boolean isOwner(Usuario usuario, Cliente cliente) {
        return cliente.getUsuario().getId().equals(usuario.getId());
    }

    public void validateAccess(Usuario usuario, Cliente cliente) {
        if (!isAdmin(usuario) && !isOwner(usuario, cliente)) {
            throw new AccessDeniedException("Acesso negado! Você não tem permissão para acessar este recurso.");
        }
    }
}
