package br.com.cdb.bancodigital.adapter.input.controller;

import br.com.cdb.bancodigital.adapter.input.dto.UsuarioDTO;
import br.com.cdb.bancodigital.adapter.input.mapper.UsuarioDTOMapper;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.port.in.auth.AuthUseCase;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ConstantUtils.AUTENTICACAO)
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthUseCase authUseCase;
    private final UsuarioDTOMapper usuarioDTOMapper;

    // Endpoint para cadastro de usuário (retorna token JWT após registro)
    @PostMapping(ConstantUtils.SIGNIN)
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioDTO dto) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_REGISTRAR);

        Usuario usuario = usuarioDTOMapper.toEntity(dto);
        UsuarioDTO response = usuarioDTOMapper.toDTO(authUseCase.registrar(usuario));
        log.info(ConstantUtils.SUCESSO_REGISTRAR);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(response);
    }

    // Endpoint para login de usuário (retorna token JWT)
    @PostMapping(ConstantUtils.LOGIN)
    public ResponseEntity<UsuarioDTO> autenticar(@Valid @RequestBody UsuarioDTO dto) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_AUTENTICACAO);

        Usuario usuario = usuarioDTOMapper.toEntity(dto);
        UsuarioDTO response = usuarioDTOMapper.toDTO(authUseCase.autenticar(usuario));
        log.info(ConstantUtils.SUCESSO_AUTENTICACAO);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(response);
    }

}
