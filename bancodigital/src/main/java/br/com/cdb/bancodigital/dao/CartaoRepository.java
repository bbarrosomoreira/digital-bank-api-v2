package br.com.cdb.bancodigital.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigital.model.Cartao;
import br.com.cdb.bancodigital.model.Usuario;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
	
	boolean existsByContaId(Long contaId);
	
	boolean existsByContaClienteId(Long clienteId);
	
	boolean existsByNumeroCartao(String numeroCartao);
	
	List<Cartao> findByContaClienteUsuario(Usuario usuario);
	
	//QUERY PARA PEGAR POR CLIENTE QUE SERIA UM JOIN DA TABELA CONTA
	List<Cartao> findByContaClienteId(Long clienteId);

	List<Cartao> findByContaId(Long contaId);
	
	@Query("SELECT ccr FROM Cartao ccr WHERE ccr.conta.id = :contaId")
	List<Cartao> findCartaoByContaId(@Param("contaId") Long contaId);
	
	@Query("SELECT ccr FROM Cartao ccr WHERE ccr.id = :id")
	Optional<Cartao> findCartaoById(@Param("id") Long id);
	
	@Query("SELECT cdb FROM Cartao cdb WHERE cdb.conta.id = :contaId")
	List<Cartao> findCartaoByContaId(@Param("contaId") Long contaId);
	
	@Query("SELECT cdb FROM Cartao cdb WHERE cdb.id = :id")
	Optional<Cartao> findCartaoById(@Param("id") Long id);
	
}
