package br.com.cdb.bancodigital.application.core.service.auth;

import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.port.in.JwtUseCase;
import br.com.cdb.bancodigital.application.port.in.auth.AuthUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.UsuarioRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.config.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService implements AuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final JwtUseCase jwtUseCase;

    public Usuario registrar(Usuario usuario) {

        // verificar se email está em uso
        log.info(ConstantUtils.LOG_VERIFICANDO_EMAIL_CADASTRADO);
        if (usuarioRepository.existWithEmail(usuario.getEmail())) {
            log.error(ConstantUtils.LOG_EMAIL_JA_CADASTRADO);
            throw new ResourceAlreadyExistsException(ConstantUtils.EXC_EMAIL_JA_CADASTRADO);
        }
        log.info(ConstantUtils.LOG_EMAIL_DISPONIVEL);

        // criar novo usuário e save no banco
        log.info(ConstantUtils.LOG_CRIANDO_NOVO_USUARIO);
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        Usuario novoUsuario = usuarioRepository.add(usuario.getEmail(), senhaCriptografada, usuario.getRole());
        log.info(ConstantUtils.LOG_USUARIO_CRIADO_SUCESSO);

        try {
            // gerar token
            log.info(ConstantUtils.LOG_GERANDO_TOKEN);
            String token = jwtUseCase.gerarToken(novoUsuario);
            log.info(ConstantUtils.LOG_TOKEN_GERADO_SUCESSO);

            return new Usuario(token); //ajuste para retornar apenas o token
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

    public Usuario autenticar(Usuario usuario) {

        // autenticar usuário
        log.info(ConstantUtils.LOG_AUTENTICANDO_USUARIO);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha()));
        log.info(ConstantUtils.LOG_USUARIO_AUTENTICADO_SUCESSO);

        // buscar usuário
        log.info(ConstantUtils.LOG_BUSCANDO_USUARIO_BANCO);
        Usuario usuarioAuth = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioAuth == null) {
            log.error(ConstantUtils.ERRO_BUSCA_USUARIO, usuario.getEmail());
            throw new ResourceNotFoundException(ConstantUtils.ERRO_BUSCA_USUARIO);
        }

        log.info(ConstantUtils.LOG_USUARIO_ENCONTRADO_SUCESSO);

        try {
            // gerar token
            log.info(ConstantUtils.LOG_GERANDO_TOKEN);
            String token = jwtUseCase.gerarToken(usuario);
            log.info(ConstantUtils.LOG_TOKEN_GERADO_SUCESSO);

            return new Usuario(token);

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
