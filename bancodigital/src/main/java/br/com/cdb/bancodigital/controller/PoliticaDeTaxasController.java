package br.com.cdb.bancodigital.controller;

import java.util.List;

import lombok.AllArgsConstructor;
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
public class PoliticaDeTaxasController {
	
	private final PoliticaDeTaxasService parametrosService;
	
	// só admin pode verificar essas políticas
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{categoria}")
	public ResponseEntity<PoliticaDeTaxas> buscarPorCategoria(
			@PathVariable String categoria){
		try {
			CategoriaCliente categoriaEnum = CategoriaCliente.valueOf(categoria.toUpperCase());
			return ResponseEntity.ok(parametrosService.buscarParametosPorCategoria(categoriaEnum));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	// só admin pode verificar essas políticas
	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PoliticaDeTaxas>> listarTodos() {
        return ResponseEntity.ok(parametrosService.listarTodosParametros());
    }

}
