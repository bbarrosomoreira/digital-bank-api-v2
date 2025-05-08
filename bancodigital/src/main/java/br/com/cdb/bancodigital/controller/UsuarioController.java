package br.com.cdb.bancodigital.controller;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UsuarioController {
	
	@GetMapping("/me")
	public ResponseEntity<UsuarioResponse> getUsuarioLogado(@AuthenticationPrincipal Usuario usuario) {
		long startTime = System.currentTimeMillis();
		log.info("Buscando informações do usuário logado.");

		UsuarioResponse dto = new UsuarioResponse(usuario);
		log.info("Informações do usuário logado obtidas com sucesso.");

		long endTime = System.currentTimeMillis();
		log.info("Busca de informações do usuário logado concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(dto);
	}
}
