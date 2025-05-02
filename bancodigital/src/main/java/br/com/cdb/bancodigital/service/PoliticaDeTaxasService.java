package br.com.cdb.bancodigital.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;

@Service
@RequiredArgsConstructor
public class PoliticaDeTaxasService {
	
	private final PoliticaDeTaxasDAO parametrosDAO;

	public PoliticaDeTaxas buscarParametosPorCategoria(CategoriaCliente categoria) {
		return parametrosDAO.findByCategoria(categoria)
				.orElseThrow(()-> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
	}
	
	public List<PoliticaDeTaxas> listarTodosParametros(){
		return parametrosDAO.findAll();
	}
}
