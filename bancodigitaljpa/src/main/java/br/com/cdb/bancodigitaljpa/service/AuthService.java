package br.com.cdb.bancodigitaljpa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljpa.dto.LoginDTO;
import br.com.cdb.bancodigitaljpa.dto.RegistroUsuarioDTO;
import br.com.cdb.bancodigitaljpa.model.Usuario;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceAlreadyExistsException;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.repository.UsuarioRepository;
import br.com.cdb.bancodigitaljpa.response.LoginResponse;
import br.com.cdb.bancodigitaljpa.security.JwtService;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse registrar(RegistroUsuarioDTO dto) {
        // verificar se email está em uso
    	Optional<Usuario> usuarioExistente = usuarioRepository.buscarUsuarioPorEmail(dto.getEmail());
    	if(usuarioExistente.isPresent()) {
    		throw new ResourceAlreadyExistsException("E-mail já cadastrado");
    	}
    	
    	// criar e salvar novo usuário
    	Usuario novoUsuario = new Usuario();
    	novoUsuario.setEmail(dto.getEmail());
    	novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
    	novoUsuario.setRole(dto.getRole());
    	
    	// gerar token
    	String token = jwtService.gerarToken(novoUsuario);
    	
        return new LoginResponse(token);
    }

    public LoginResponse autenticar(LoginDTO dto) {
    	// autenticar usuário
    	authenticationManager.authenticate(
    			new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
    	
    	// buscar usuário
    	Usuario usuario = usuarioRepository.buscarUsuarioPorEmail(dto.getEmail());
    	
    	// gerar token
    	String token = jwtService.gerarToken(usuario);
    	
        return new LoginResponse(token);
    }
}
