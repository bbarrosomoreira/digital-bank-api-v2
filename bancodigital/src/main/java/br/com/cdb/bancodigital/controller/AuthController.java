package br.com.cdb.bancodigital.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.dto.LoginDTO;
import br.com.cdb.bancodigital.dto.UsuarioDTO;
import br.com.cdb.bancodigital.dto.response.LoginResponse;
import br.com.cdb.bancodigital.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
	
	private final AuthService authService;

	// Endpoint para cadastro de usuário (retorna token JWT após registro)
	@PostMapping("/signin")
	public ResponseEntity<LoginResponse> registrar(@Valid @RequestBody UsuarioDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando registro de novo usuário.");

		LoginResponse response = authService.registrar(dto);
		log.info("Usuário registrado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Registro de usuário concluído em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	
	// Endpoint para login de usuário (retorna token JWT)
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> autenticar(@Valid @RequestBody LoginDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando autenticação de usuário.");

		LoginResponse response = authService.autenticar(dto);
		log.info("Usuário autenticado com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Autenticação concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(response);
	}
	


}
