package br.com.cdb.bancodigitaljpa.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.model.ContaBase;
import br.com.cdb.bancodigitaljpa.model.ContaCorrente;
import br.com.cdb.bancodigitaljpa.model.ContaPoupanca;
import br.com.cdb.bancodigitaljpa.model.Usuario;

@Repository
public interface ContaRepository extends JpaRepository<ContaBase, Long> {
	
	boolean existsByClienteId(Long clienteId);
	
	List<ContaBase> findByClienteId(Long clienteId);
	List<ContaBase> findByClienteUsuario(Usuario usuario);
	
	@Query("SELECT cc FROM ContaCorrente cc WHERE cc.cliente.id = :clienteId")
	List<ContaCorrente> findContasCorrenteByClienteId(@Param("clienteId") Long clienteId);
	
	@Query("SELECT cc FROM ContaCorrente cc WHERE cc.id = :id")
	Optional<ContaCorrente> findContaCorrenteById(@Param("id") Long id); 
	
	@Query("SELECT cp FROM ContaPoupanca cp WHERE cp.cliente.id = :clienteId")
	List<ContaPoupanca> findContasPoupancaByClienteId(@Param("clienteId") Long clienteId);
	
	@Query("SELECT cp FROM ContaPoupanca cp WHERE cp.id = :id")
	Optional<ContaPoupanca> findContaPoupancaById(@Param("id") Long id);
	
}
