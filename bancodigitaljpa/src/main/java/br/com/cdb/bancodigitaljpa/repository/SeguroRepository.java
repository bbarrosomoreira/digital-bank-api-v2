package br.com.cdb.bancodigitaljpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.entity.SeguroBase;

@Repository
public interface SeguroRepository extends JpaRepository<SeguroBase, Long>{
	
	//Listar seguros por cartao de credito
	@Query("SELECT seguro FROM SeguroBase seguro WHERE seguro.cartaoCredito.id = :cartaoId")
	List<SeguroBase> findByCartaoCreditoId (@Param("cartaId") Long cartaoId);
	
	//Listar seguros por cliente
	@Query("SELECT seguro FROM SeguroBase seguro WHERE seguro.cartaoCredito.conta.cliente.id = :clienteId")
	List<SeguroBase> findByClienteId (@Param("clienteId") Long clienteId);

}
