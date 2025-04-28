package br.com.cdb.bancodigitaljpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.model.CartaoBase;
import br.com.cdb.bancodigitaljpa.model.CartaoCredito;
import br.com.cdb.bancodigitaljpa.model.CartaoDebito;
import br.com.cdb.bancodigitaljpa.model.Usuario;

@Repository
public interface CartaoRepository extends JpaRepository<CartaoBase, Long> {
	
	boolean existsByContaId(Long contaId);
	
	boolean existsByContaClienteId(Long clienteId);
	
	boolean existsByNumeroCartao(String numeroCartao);
	
	List<CartaoBase> findByContaClienteUsuario(Usuario usuario);
	
	//QUERY PARA PEGAR POR CLIENTE QUE SERIA UM JOIN DA TABELA CONTA
	List<CartaoBase> findByContaClienteId(Long clienteId);

	List<CartaoBase> findByContaId(Long contaId);
	
	@Query("SELECT ccr FROM CartaoCredito ccr WHERE ccr.conta.id = :contaId")
	List<CartaoCredito> findCartaoCreditoByContaId(@Param("contaId") Long contaId);
	
	@Query("SELECT ccr FROM CartaoCredito ccr WHERE ccr.id = :id")
	Optional<CartaoCredito> findCartaoCreditoById(@Param("id") Long id);
	
	@Query("SELECT cdb FROM CartaoDebito cdb WHERE cdb.conta.id = :contaId")
	List<CartaoDebito> findCartaoDebitoByContaId(@Param("contaId") Long contaId);
	
	@Query("SELECT cdb FROM CartaoDebito cdb WHERE cdb.id = :id")
	Optional<CartaoDebito> findCartaoDebitoById(@Param("id") Long id);
	
}
