package br.com.cdb.bancodigital.application.port.in.auth;

import br.com.cdb.bancodigital.application.core.domain.dto.LoginDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.UsuarioDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.response.LoginResponse;

public interface AuthUseCase {

    LoginResponse registrar(UsuarioDTO dto);
    LoginResponse autenticar(LoginDTO dto);
}
