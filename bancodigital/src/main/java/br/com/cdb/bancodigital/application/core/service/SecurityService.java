package br.com.cdb.bancodigital.application.core.service;

import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Role;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SecurityService implements SecurityUseCase {

    public boolean isAdmin(Usuario usuario) {
        log.info(ConstantUtils.INICIO_VERIFICACAO_ADMIN, usuario.getId());
        boolean result = usuario.getRole() == Role.ADMIN;
        log.info(ConstantUtils.RESULTADO_VERIFICACAO_ADMIN, usuario.getId(), result);
        return result;
    }

    public boolean isOwner(Usuario usuario, Cliente cliente) {
        log.info(ConstantUtils.INICIO_VERIFICACAO_OWNER, usuario.getId(), cliente.getId());
        boolean result = cliente.getUsuario().getId().equals(usuario.getId());
        log.info(ConstantUtils.RESULTADO_VERIFICACAO_OWNER, usuario.getId(), cliente.getId(), result);
        return result;
    }

    public void validateAccess(Usuario usuario, Cliente cliente) {
        log.info(ConstantUtils.INICIO_VALIDACAO_ACESSO, usuario.getId(), cliente.getId());
        if (!isAdmin(usuario) && !isOwner(usuario, cliente)) {
            log.warn(ConstantUtils.ACESSO_NEGADO_LOG, usuario.getId(), cliente.getId());
            throw new AccessDeniedException(ConstantUtils.MENSAGEM_ACESSO_NEGADO);
        }
        log.info(ConstantUtils.ACESSO_PERMITIDO_LOG, usuario.getId(), cliente.getId());
    }
}
