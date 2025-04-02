package br.com.cdb.bancodigitaljpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitaljpa.entity.Parametros;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.service.ParametrosService;

@RestController
@RequestMapping("/parametros")
public class ParametrosController {
	
	@Autowired
	private ParametrosService parametrosService;
	
	@GetMapping("/{categoria}")
	public ResponseEntity<Parametros> buscarPorCategoria(
			@PathVariable String categoria){
		try {
			CategoriaCliente categoriaEnum = CategoriaCliente.valueOf(categoria.toUpperCase());
			return ResponseEntity.ok(parametrosService.buscarParametosPorCategoria(categoriaEnum));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
    @GetMapping("/listAll")
    public ResponseEntity<List<Parametros>> listarTodos() {
        return ResponseEntity.ok(parametrosService.listarTodosParametros());
    }

}
