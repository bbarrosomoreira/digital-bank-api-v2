//package br.com.cdb.bancodigitaljpa.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import br.com.cdb.bancodigitaljpa.entity.SeguroBase;
//
//@Repository
//public interface SeguroRepository extends JpaRepository<SeguroBase, Long>{
//	
//	//Listar seguros por cartao de credito
//	List<SeguroBase> findByCartaoCreditoId (Long cartaoCreditoId);
//	
//	//Listar seguros por cliente
//	List<SeguroBase> findByCartaoCreditoContaClienteId (Long clienteId);
//
//}
