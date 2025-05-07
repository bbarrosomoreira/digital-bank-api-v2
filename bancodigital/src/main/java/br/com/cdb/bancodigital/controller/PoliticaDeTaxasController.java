package br.com.cdb.bancodigital.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.service.PoliticaDeTaxasService;

@RestController
@RequestMapping("/parametros")
@AllArgsConstructor
@Slf4j
public class PoliticaDeTaxasController {
	
	private final PoliticaDeTaxasService parametrosService;
	
	@GetMapping("/{categoria}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PoliticaDeTaxas> buscarPorCategoria(
			@PathVariable CategoriaCliente categoria){
		long startTime = System.currentTimeMillis();
		log.info("Iniciando busca de parâmetros para a categoria: {}.", categoria);

		PoliticaDeTaxas parametros = parametrosService.buscarParametosPorCategoria(categoria);
		log.info("Parâmetros encontrados para a categoria: {}.", categoria);

		long endTime = System.currentTimeMillis();
		log.info("Busca de parâmetros concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(parametros);
	}
	
    @GetMapping
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PoliticaDeTaxas>> listarTodos() {
		long startTime = System.currentTimeMillis();
		log.info("Iniciando listagem de todos os parâmetros de taxas.");

		List<PoliticaDeTaxas> parametros = parametrosService.listarTodosParametros();
		log.info("Parametros encontrados");

		long endTime = System.currentTimeMillis();
		log.info("Listagem de parâmetros concluída em {} ms.", endTime - startTime);
		return ResponseEntity.ok(parametros);
    }

}
