package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dao.UsuarioDAO;
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

import java.util.Optional;

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
        log.info("Verificando se o e-mail já está cadastrado");
        Optional<Usuario> usuarioExistente = usuarioDAO.buscarUsuarioPorEmail(dto.getEmail());
        if (usuarioExistente.isPresent()) {
            log.error("E-mail já cadastrado");
            throw new ResourceAlreadyExistsException("E-mail já cadastrado");
        }
        log.info("E-mail disponível");

        // criar novo usuário e salvar no banco
        log.info("Criando novo usuário");
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        Usuario novoUsuario = usuarioDAO.criarUsuario(dto.getEmail(), senhaCriptografada, dto.getRole());
        log.info("Usuário criado com sucesso");

        try {
            // gerar token
            log.info("Gerando token");
            String token = jwtService.gerarToken(novoUsuario);
            log.info("Token gerado com sucesso");

            return new LoginResponse(token);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao gerar token: argumento inválido", e);
            throw new ValidationException("Erro ao gerar token: argumento inválido.");
        } catch (JwtException e) {
            log.error("Erro ao gerar token JWT", e);
            throw new ValidationException("Erro ao gerar token JWT.");
        } catch (Exception e) {
            log.error("Erro inesperado ao autenticar usuário", e);
            throw new ValidationException("Erro inesperado ao autenticar usuário.");
        }

    }

    public LoginResponse autenticar(LoginDTO dto) {

        // autenticar usuário
        log.info("Autenticando usuário");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
        log.info("Usuário autenticado com sucesso");

        // buscar usuário
        log.info("Buscando usuário no banco de dados");
        Usuario usuario = usuarioDAO.buscarUsuarioPorEmailOuErro(dto.getEmail());
        log.info("Usuário encontrado com sucesso");

        try {
            // gerar token
            log.info("Gerando token");
            String token = jwtService.gerarToken(usuario);
            log.info("Token gerado com sucesso");

            return new LoginResponse(token);

        } catch (IllegalArgumentException e) {
            log.error("Erro ao gerar token: argumento inválido", e);
            throw new ValidationException("Erro ao gerar token: argumento inválido.");
        } catch (JwtException e) {
            log.error("Erro ao gerar token JWT", e);
            throw new ValidationException("Erro ao gerar token JWT.");
        } catch (Exception e) {
            log.error("Erro inesperado ao autenticar usuário", e);
            throw new ValidationException("Erro inesperado ao autenticar usuário.");
        }

    }
}
