package br.com.cdb.bancodigitaljpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.entity.Parametros;
import br.com.cdb.bancodigitaljpa.enums.CategoriaCliente;

@Repository
public interface ParametrosRepository extends JpaRepository<Parametros, Long>{

	Optional<Parametros> findByCategoria(CategoriaCliente categoria);
}
