package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.Usuario;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	boolean existsByClienteId(Long clienteId);
	
	List<Conta> findByClienteId(Long clienteId);
	List<Conta> findByClienteUsuario(Usuario usuario);
	
	@Query("SELECT cc FROM Conta cc WHERE cc.cliente.id = :clienteId")
	List<Conta> findContasByClienteId(@Param("clienteId") Long clienteId);
	
	@Query("SELECT cc FROM Conta cc WHERE cc.id = :id")
	Optional<Conta> findContaById(@Param("id") Long id);
	
	@Query("SELECT cp FROM Conta cp WHERE cp.cliente.id = :clienteId")
	List<Conta> findContasByClienteId(@Param("clienteId") Long clienteId);
	
	@Query("SELECT cp FROM ContaPoupanca cp WHERE cp.id = :id")
	Optional<Conta> findContaById(@Param("id") Long id);
	
}
