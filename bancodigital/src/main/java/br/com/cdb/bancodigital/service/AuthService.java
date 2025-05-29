package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dao.UsuarioDAO;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.dto.LoginDTO;
import br.com.cdb.bancodigital.dto.UsuarioDTO;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigital.dto.response.LoginResponse;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse registrar(UsuarioDTO dto) {

        // verificar se email está em uso
        log.info(ConstantUtils.LOG_VERIFICANDO_EMAIL_CADASTRADO);
        if (usuarioDAO.existByEmail(dto.getEmail())) {
            log.error(ConstantUtils.LOG_EMAIL_JA_CADASTRADO);
            throw new ResourceAlreadyExistsException(ConstantUtils.EXC_EMAIL_JA_CADASTRADO);
        }
        log.info(ConstantUtils.LOG_EMAIL_DISPONIVEL);

        // criar novo usuário e salvar no banco
        log.info(ConstantUtils.LOG_CRIANDO_NOVO_USUARIO);
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        Usuario novoUsuario = usuarioDAO.criarUsuario(dto.getEmail(), senhaCriptografada, dto.getRole());
        log.info(ConstantUtils.LOG_USUARIO_CRIADO_SUCESSO);

        try {
            // gerar token
            log.info(ConstantUtils.LOG_GERANDO_TOKEN);
            String token = jwtService.gerarToken(novoUsuario);
            log.info(ConstantUtils.LOG_TOKEN_GERADO_SUCESSO);

            return new LoginResponse(token);
        } catch (IllegalArgumentException e) {
            log.error(ConstantUtils.LOG_ERRO_GERAR_TOKEN_ARG, e);
            throw new ValidationException(ConstantUtils.EXC_ERRO_GERAR_TOKEN_ARG);
        } catch (JwtException e) {
            log.error(ConstantUtils.LOG_ERRO_GERAR_TOKEN_JWT, e);
            throw new ValidationException(ConstantUtils.EXC_ERRO_GERAR_TOKEN_JWT);
        } catch (Exception e) {
            log.error(ConstantUtils.LOG_ERRO_INESPERADO_AUTENTICAR, e);
            throw new ValidationException(ConstantUtils.EXC_ERRO_INESPERADO_AUTENTICAR);
        }

    }

    public LoginResponse autenticar(LoginDTO dto) {

        // autenticar usuário
        log.info(ConstantUtils.LOG_AUTENTICANDO_USUARIO);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
        log.info(ConstantUtils.LOG_USUARIO_AUTENTICADO_SUCESSO);

        // buscar usuário
        log.info(ConstantUtils.LOG_BUSCANDO_USUARIO_BANCO);
        Usuario usuario = usuarioDAO.buscarUsuarioPorEmail(dto.getEmail());
        if (usuario == null) {
            log.error(ConstantUtils.ERRO_BUSCA_USUARIO, dto.getEmail());
            throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_USUARIO);
        }

        log.info(ConstantUtils.LOG_USUARIO_ENCONTRADO_SUCESSO);

        try {
            // gerar token
            log.info(ConstantUtils.LOG_GERANDO_TOKEN);
            String token = jwtService.gerarToken(usuario);
            log.info(ConstantUtils.LOG_TOKEN_GERADO_SUCESSO);

            return new LoginResponse(token);

        } catch (IllegalArgumentException e) {
            log.error(ConstantUtils.LOG_ERRO_GERAR_TOKEN_ARG, e);
            throw new ValidationException(ConstantUtils.EXC_ERRO_GERAR_TOKEN_ARG);
        } catch (JwtException e) {
            log.error(ConstantUtils.LOG_ERRO_GERAR_TOKEN_JWT, e);
            throw new ValidationException(ConstantUtils.EXC_ERRO_GERAR_TOKEN_JWT);
        } catch (Exception e) {
            log.error(ConstantUtils.LOG_ERRO_INESPERADO_AUTENTICAR, e);
            throw new ValidationException(ConstantUtils.EXC_ERRO_INESPERADO_AUTENTICAR);
        }

    }
}
