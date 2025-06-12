package br.com.cdb.bancodigital.application.port.out.repository;

import br.com.cdb.bancodigital.application.core.domain.entity.PoliticaDeTaxas;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;

import java.util.List;
import java.util.Optional;

public interface PoliticaDeTaxasRepository {
    List<PoliticaDeTaxas> findAll();
    Optional<PoliticaDeTaxas> findByCategoria(CategoriaCliente categoria);
}
