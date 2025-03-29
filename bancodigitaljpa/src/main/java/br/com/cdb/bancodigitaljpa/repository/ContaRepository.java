package br.com.cdb.bancodigitaljpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.entity.ContaBase;

@Repository
public interface ContaRepository extends JpaRepository<ContaBase, Long> {
	
	List<ContaBase> findByClienteId(Long id_cliente);
		
}
