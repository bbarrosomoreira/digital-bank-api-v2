package br.com.cdb.bancodigital.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigital.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;

@Repository
public interface PoliticaDeTaxasDAO extends JpaRepository<PoliticaDeTaxas, Long>{

	Optional<PoliticaDeTaxas> findByCategoria(CategoriaCliente categoria);
}
