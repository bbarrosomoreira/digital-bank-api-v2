package br.com.cdb.bancodigitaljpa.service;

import org.springframework.stereotype.Component;

import br.com.cdb.bancodigitaljpa.entity.Cliente;
import br.com.cdb.bancodigitaljpa.entity.Usuario;
import br.com.cdb.bancodigitaljpa.enums.Role;
import br.com.cdb.bancodigitaljpa.exceptions.custom.AccessDeniedException;

@Component 
public class SecurityService {

    public boolean isAdmin(Usuario usuario) {
        return usuario.getRole() == Role.ADMIN;
    }

    public boolean isOwner(Usuario usuario, Cliente cliente) {
        return cliente.getUsuario().getId().equals(usuario.getId());
    }

    public void validateAccess(Usuario usuario, Cliente cliente) {
        if (!isAdmin(usuario) && !isOwner(usuario, cliente)) {
            throw new AccessDeniedException("Você não tem permissão para acessar este recurso.");
        }
    }
}
