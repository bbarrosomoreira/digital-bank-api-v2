package br.com.cdb.bancodigital.controller;

import java.util.List;

import br.com.cdb.bancodigital.application.core.domain.dto.PoliticaTaxaDTO;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.cdb.bancodigital.application.core.domain.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.service.PoliticaDeTaxasService;

@RestController
@RequestMapping(ConstantUtils.POLITICA_TAXAS)
@AllArgsConstructor
@Slf4j
public class PoliticaDeTaxasController {
	
	private final PoliticaDeTaxasService parametrosService;
	
	@GetMapping(ConstantUtils.GET_CATEGORIA)
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	public ResponseEntity<PoliticaDeTaxas> buscarPorCategoria(
			@Valid @RequestBody PoliticaTaxaDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_BUSCA_POLITICA_TAXAS, dto.getCategoria());
		PoliticaDeTaxas parametros = parametrosService.buscarParametosPorCategoria(dto.getCategoria());
		log.info(ConstantUtils.SUCESSO_BUSCA_POLITICA_TAXAS, dto.getCategoria());

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(parametros);
	}
	
    @GetMapping
	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
    public ResponseEntity<List<PoliticaDeTaxas>> listarTodos() {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_LISTAGEM_POLITICA_TAXAS);

		List<PoliticaDeTaxas> parametros = parametrosService.listarTodosParametros();
		log.info(ConstantUtils.SUCESSO_LISTAGEM_POLITICA_TAXAS);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(parametros);
    }

}
