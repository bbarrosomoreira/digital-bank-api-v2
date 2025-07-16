package br.com.cdb.bancodigital.adapter.input.controller;

import br.com.cdb.bancodigital.adapter.input.mapper.UsuarioDTOMapper;
import br.com.cdb.bancodigital.application.core.domain.dto.LoginDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.UsuarioDTO;
import br.com.cdb.bancodigital.application.core.domain.dto.response.LoginResponse;
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
    public ResponseEntity<LoginResponse> registrar(@Valid @RequestBody UsuarioDTO dto) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_REGISTRAR);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(authUseCase.registrar(dto));
    }

    // Endpoint para login de usuário (retorna token JWT)
    @PostMapping(ConstantUtils.LOGIN)
    public ResponseEntity<LoginResponse> autenticar(@Valid @RequestBody LoginDTO dto) {
        long startTime = System.currentTimeMillis();
        log.info(ConstantUtils.INICIO_AUTENTICACAO);

        long endTime = System.currentTimeMillis();
        log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
        return ResponseEntity.ok(authUseCase.autenticar(dto));
    }

}
