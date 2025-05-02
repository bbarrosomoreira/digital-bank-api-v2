package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dao.UsuarioDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.dto.LoginDTO;
import br.com.cdb.bancodigital.dto.UsuarioDTO;
import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigital.dto.response.LoginResponse;
import br.com.cdb.bancodigital.security.JwtService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;
    
    public LoginResponse registrar(UsuarioDTO dto) {
        // verificar se email está em uso
		Optional<Usuario> usuarioExistente = usuarioDAO.buscarUsuarioPorEmail(dto.getEmail());
		if (usuarioExistente.isPresent()) {
			throw new ResourceAlreadyExistsException("E-mail já cadastrado");
		}

		// criar novo usuário e salvar no banco
		String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
		Usuario novoUsuario = usuarioDAO.criarUsuario(dto.getEmail(), senhaCriptografada, dto.getRole());

		// gerar token
    	String token = jwtService.gerarToken(novoUsuario);
    	
        return new LoginResponse(token);
    }

    public LoginResponse autenticar(LoginDTO dto) {
    	// autenticar usuário
    	authenticationManager.authenticate(
    			new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
    	
    	// buscar usuário
    	Usuario usuario = usuarioDAO.buscarUsuarioPorEmailOuErro(dto.getEmail());
    	
    	// gerar token
    	String token = jwtService.gerarToken(usuario);
    	
        return new LoginResponse(token);
    }
}
