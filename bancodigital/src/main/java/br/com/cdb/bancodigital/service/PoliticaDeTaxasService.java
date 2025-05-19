package br.com.cdb.bancodigital.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Service
@AllArgsConstructor
public class PoliticaDeTaxasService {
	
	private final PoliticaDeTaxasDAO parametrosDAO;

	public PoliticaDeTaxas buscarParametosPorCategoria(CategoriaCliente categoria) {
		return parametrosDAO.findByCategoria(categoria)
				.orElseThrow(() -> new ResourceNotFoundException(
					String.format(ConstantUtils.ERRO_BUSCA_POLITICA_TAXAS, categoria)
				));
	}
	
	public List<PoliticaDeTaxas> listarTodosParametros(){
		return parametrosDAO.findAll();
	}
}
