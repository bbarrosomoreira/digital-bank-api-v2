package br.com.cdb.bancodigital.adapter.input.controller;

import br.com.cdb.bancodigital.adapter.input.dto.UsuarioDTO;
import br.com.cdb.bancodigital.adapter.input.mapper.UsuarioDTOMapper;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.port.in.usuario.UsuarioUseCase;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ConstantUtils.USUARIO)
@AllArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioDTOMapper usuarioDTOMapper;

    @GetMapping(ConstantUtils.GET_USUARIO)
    public ResponseEntity<UsuarioDTO> getUsuarioLogado(@AuthenticationPrincipal Usuario usuario) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_BUSCA_USUARIO);

        UsuarioDTO dto = usuarioDTOMapper.toDTO(usuario);
        log.info(ConstantUtils.SUCESSO_BUSCA_USUARIO);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize(ConstantUtils.ROLE_ADMIN)
    @DeleteMapping(ConstantUtils.DELETE_USUARIO)
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id_usuario) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_DELETE_USUARIO, id_usuario);

        usuarioUseCase.deleteUsuario(id_usuario);
        log.info(ConstantUtils.SUCESSO_DELETE_USUARIO, id_usuario);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.noContent().build();
    }

}
