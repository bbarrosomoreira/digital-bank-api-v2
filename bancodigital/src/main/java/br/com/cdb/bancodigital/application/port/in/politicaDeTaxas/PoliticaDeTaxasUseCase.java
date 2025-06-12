package br.com.cdb.bancodigital.application.port.in.politicaDeTaxas;

import br.com.cdb.bancodigital.application.core.domain.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;

import java.util.List;

public interface PoliticaDeTaxasUseCase {
    PoliticaDeTaxas buscarParametosPorCategoria(CategoriaCliente categoria);
    List<PoliticaDeTaxas> listarTodosParametros();
}
