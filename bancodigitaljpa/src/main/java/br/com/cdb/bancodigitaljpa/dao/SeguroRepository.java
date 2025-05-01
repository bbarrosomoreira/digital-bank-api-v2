package br.com.cdb.bancodigitaljpa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.model.Seguro;
import br.com.cdb.bancodigitaljpa.model.Usuario;

@Repository
public interface SeguroRepository extends JpaRepository<Seguro, Long>{
	
	boolean existsByCartaoCreditoId(Long cartaoId);
	
	boolean existsByCartaoCreditoContaClienteId(@Param("clienteId") Long clienteId);
	
	//Listar seguros por cartao de credito
	@Query("SELECT seguro FROM SeguroBase seguro WHERE seguro.cartaoCredito.id = :cartaoCreditoId")
	List<Seguro> findByCartaoCreditoId (@Param("cartaoCreditoId") Long cartaoCreditoId);
	
	//Listar seguros por cliente
	@Query("SELECT seguro FROM SeguroBase seguro WHERE seguro.cartaoCredito.conta.cliente.id = :clienteId")
	List<Seguro> findByClienteId (@Param("clienteId") Long clienteId);

	List<Seguro> findByCartaoCreditoContaClienteUsuario(Usuario usuario);
}
