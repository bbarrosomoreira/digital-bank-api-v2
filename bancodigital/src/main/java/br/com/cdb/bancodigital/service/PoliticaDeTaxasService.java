package br.com.cdb.bancodigital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;

@Service
public class PoliticaDeTaxasService {
	
	@Autowired
	private PoliticaDeTaxasDAO parametrosRepository;
	

	public PoliticaDeTaxas buscarParametosPorCategoria(CategoriaCliente categoria) {
		return parametrosRepository.findByCategoria(categoria)
				.orElseThrow(()-> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
	}
	
	public List<PoliticaDeTaxas> listarTodosParametros(){
		return parametrosRepository.findAll();
	}
}
