package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.utils.ConstantUtils;
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
@RequestMapping(ConstantUtils.USUARIO)
@Slf4j
public class UsuarioController {
	
	@GetMapping(ConstantUtils.GET_USUARIO)
	public ResponseEntity<UsuarioResponse> getUsuarioLogado(@AuthenticationPrincipal Usuario usuario) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_USUARIO);

		UsuarioResponse dto = new UsuarioResponse(usuario);
		log.info(ConstantUtils.SUCESSO_BUSCA_USUARIO);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(dto);
	}
}
