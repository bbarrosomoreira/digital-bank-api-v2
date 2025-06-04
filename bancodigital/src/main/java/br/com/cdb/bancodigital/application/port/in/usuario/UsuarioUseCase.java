package br.com.cdb.bancodigital.application.port.in.usuario;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioUseCase extends UserDetailsService {

    void deleteUsuario(Long id_usuario);

}
