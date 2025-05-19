package br.com.cdb.bancodigital.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Service
@AllArgsConstructor
@Slf4j
public class PoliticaDeTaxasService {
	
	private final PoliticaDeTaxasDAO parametrosDAO;

	public PoliticaDeTaxas buscarParametosPorCategoria(CategoriaCliente categoria) {
		log.info(ConstantUtils.INICIO_BUSCA_POLITICA_TAXAS, categoria);
		try {
			PoliticaDeTaxas politica = parametrosDAO.findByCategoria(categoria)
				.orElseThrow(() -> new ResourceNotFoundException(
					String.format(ConstantUtils.ERRO_BUSCA_POLITICA_TAXAS, categoria)
				));
			log.info(ConstantUtils.SUCESSO_BUSCA_POLITICA_TAXAS, categoria);
			return politica;
		} catch (ResourceNotFoundException ex) {
			log.error(ConstantUtils.ERRO_BUSCA_POLITICA_TAXAS, categoria);
			throw ex;
		}
	}
	
	public List<PoliticaDeTaxas> listarTodosParametros(){
		log.info(ConstantUtils.INICIO_LISTAGEM_POLITICA_TAXAS);
		try {
			List<PoliticaDeTaxas> lista = parametrosDAO.findAll();
			log.info(ConstantUtils.SUCESSO_LISTAGEM_POLITICA_TAXAS);
			return lista;
		} catch (Exception ex) {
			log.error(ConstantUtils.ERRO_LISTAGEM_POLITICA_TAXAS);
			throw ex;
		}
	}
}
