package br.com.cdb.bancodigital.application.port.in.politicaDeTaxas;

import br.com.cdb.bancodigital.application.core.domain.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.application.core.domain.model.enums.CategoriaCliente;

import java.util.List;

public interface PoliticaDeTaxasUseCase {
    PoliticaDeTaxas buscarParametosPorCategoria(CategoriaCliente categoria);
    List<PoliticaDeTaxas> listarTodosParametros();
}
