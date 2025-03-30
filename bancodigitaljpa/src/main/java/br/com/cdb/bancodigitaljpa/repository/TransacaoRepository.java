package br.com.cdb.bancodigitaljpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljpa.entity.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>{

}
