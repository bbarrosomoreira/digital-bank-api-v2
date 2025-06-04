package br.com.cdb.bancodigital.application.port.out.repository;

import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Role;

import java.util.Optional;

public interface UsuarioRepository {

    Usuario add(String email, String senha, Role role);
    boolean existWithEmail(String email);
    Usuario findByEmail(String email);
    Optional<Usuario> findById(Long id);
    void update(Long id, String email, String senha, Role role);
    void delete(Long id);
}
