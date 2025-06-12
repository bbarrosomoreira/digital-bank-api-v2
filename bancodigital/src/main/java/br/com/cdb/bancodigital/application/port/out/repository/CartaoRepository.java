package br.com.cdb.bancodigital.application.port.out.repository;

import br.com.cdb.bancodigital.application.core.domain.entity.Cartao;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface CartaoRepository {
    Cartao save(Cartao cartao);
    Cartao add(Cartao cartao);
    List<Cartao> findAll();
    Optional<Cartao> findById(Long id);
    List<Cartao> findByContaId(Long contaId);
    List<Cartao> findByContaClienteUsuario(Usuario usuario);
    List<Cartao> findByContaClienteId(Long clienteId);
    void validateVinculosCartao(Long id);
    Cartao update(Cartao cartao);
    void delete(Long id);
}
