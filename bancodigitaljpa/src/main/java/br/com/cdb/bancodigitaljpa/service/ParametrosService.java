package br.com.cdb.bancodigitaljpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljpa.entity.Parametros;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.repository.ParametrosRepository;

@Service
public class ParametrosService {
	
	@Autowired
	private ParametrosRepository parametrosRepository;
	

	public Parametros buscarParametosPorCategoria(CategoriaCliente categoria) {
		return parametrosRepository.findByCategoria(categoria)
				.orElseThrow(()-> new RuntimeException("Parâmetros não encontrados para a categoria: " + categoria));
	}
	
	public List<Parametros> listarTodosParametros(){
		return parametrosRepository.findAll();
	}
}
