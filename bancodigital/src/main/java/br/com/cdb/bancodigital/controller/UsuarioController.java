package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.service.UsuarioService;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.cdb.bancodigital.model.Usuario;
import br.com.cdb.bancodigital.dto.response.UsuarioResponse;

@RestController
@RequestMapping(ConstantUtils.USUARIO)
@AllArgsConstructor
@Slf4j
public class UsuarioController {

	private final UsuarioService usuarioService;

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

	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@DeleteMapping(ConstantUtils.DELETE_USUARIO)
	public ResponseEntity<Void> deleteUsuario(@PathVariable Long id_usuario) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_DELETE_USUARIO, id_usuario);

		usuarioService.deleteUsuario(id_usuario);
		log.info(ConstantUtils.SUCESSO_DELETE_USUARIO, id_usuario);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.noContent().build();
	}
}
