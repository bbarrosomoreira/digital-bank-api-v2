package br.com.cdb.bancodigital.application.port.in.auth;

import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

public interface AuthUseCase {

    Usuario registrar(Usuario usuario);
    Usuario autenticar(Usuario usuario);
}
