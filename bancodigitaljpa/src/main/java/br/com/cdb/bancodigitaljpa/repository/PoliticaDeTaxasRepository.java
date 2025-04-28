package br.com.cdb.bancodigitaljpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.model.PoliticaDeTaxas;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;

@Repository
public interface PoliticaDeTaxasRepository extends JpaRepository<PoliticaDeTaxas, Long>{

	Optional<PoliticaDeTaxas> findByCategoria(CategoriaCliente categoria);
}
