package br.com.cdb.bancodigitaljpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.entity.ContaBase;

@Repository
public interface ContaRepository extends JpaRepository<ContaBase, Long> {
		
}
