package br.com.cdb.bancodigitaljpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljpa.model.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.model.enums.CategoriaCliente;
import br.com.cdb.bancodigitaljpa.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigitaljpa.dao.PoliticaDeTaxasRepository;

@Service
public class PoliticaDeTaxasService {
	
	@Autowired
	private PoliticaDeTaxasRepository parametrosRepository;
	

	public PoliticaDeTaxas buscarParametosPorCategoria(CategoriaCliente categoria) {
		return parametrosRepository.findByCategoria(categoria)
				.orElseThrow(()-> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
	}
	
	public List<PoliticaDeTaxas> listarTodosParametros(){
		return parametrosRepository.findAll();
	}
}
