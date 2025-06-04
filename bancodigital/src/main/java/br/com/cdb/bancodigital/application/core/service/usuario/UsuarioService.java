package br.com.cdb.bancodigital.application.core.service.usuario;

import br.com.cdb.bancodigital.application.port.in.usuario.UsuarioUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.UsuarioRepository;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UsuarioService implements UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email);
    }

    // DELETE | Deletar usuário
    public void deleteUsuario(Long id_usuario) {
        // adicionar verificação de vínculo com cliente
        log.info(ConstantUtils.INICIO_DELETE_USUARIO, id_usuario);
        usuarioRepository.delete(id_usuario);
        log.info(ConstantUtils.SUCESSO_DELETE_USUARIO, id_usuario);
    }
}
