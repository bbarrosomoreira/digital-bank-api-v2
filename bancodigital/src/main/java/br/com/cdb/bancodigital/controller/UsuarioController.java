package br.com.cdb.bancodigital.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.dto.response.UsuarioResponse;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@GetMapping("/me")
	public ResponseEntity<UsuarioResponse> getUsuarioLogado(@AuthenticationPrincipal Usuario usuario) {
		UsuarioResponse dto = new UsuarioResponse(usuario);
	    return ResponseEntity.ok(dto);
	}

	@GetMapping("/me-teste")
	public String getMe(Authentication authentication) {
	    return authentication != null ? authentication.getName() : "usuario null";
	}
}
