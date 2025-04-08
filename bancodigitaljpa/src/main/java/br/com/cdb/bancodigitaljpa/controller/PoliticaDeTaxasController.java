package br.com.cdb.bancodigitaljpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.service.PoliticaDeTaxasService;

@RestController
@RequestMapping("/parametros")
public class PoliticaDeTaxasController {
	
	@Autowired
	private PoliticaDeTaxasService parametrosService;
	
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
	
    @GetMapping
    public ResponseEntity<List<PoliticaDeTaxas>> listarTodos() {
        return ResponseEntity.ok(parametrosService.listarTodosParametros());
    }

}
