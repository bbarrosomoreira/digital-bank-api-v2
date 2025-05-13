package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dao.UsuarioDAO;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    // DELETE | Deletar usuário
    public void deleteUsuario(Long id_usuario) {
        log.info(ConstantUtils.INICIO_DELETE_USUARIO, id_usuario);
        usuarioDAO.deletarUsuario(id_usuario);
        log.info(ConstantUtils.SUCESSO_DELETE_USUARIO, id_usuario);
    }
}
