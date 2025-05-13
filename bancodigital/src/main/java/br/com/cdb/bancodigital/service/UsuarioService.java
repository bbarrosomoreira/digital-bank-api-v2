package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dao.UsuarioDAO;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UsuarioService implements UserDetailsService {

    private final UsuarioDAO usuarioDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioDAO.buscarUsuarioPorEmailOuErro(email);
    }

    // DELETE | Deletar usuário
    public void deleteUsuario(Long id_usuario) {
        // adicionar verificação de vínculo com cliente
        log.info(ConstantUtils.INICIO_DELETE_USUARIO, id_usuario);
        usuarioDAO.deletarUsuario(id_usuario);
        log.info(ConstantUtils.SUCESSO_DELETE_USUARIO, id_usuario);
    }
}
