package br.com.cdb.bancodigital.application.port.out.repository;

import br.com.cdb.bancodigital.application.core.domain.entity.Conta;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface ContaRepository {
    Conta save(Conta conta);
    Conta add(Conta conta);
    List<Conta> findAll();
    Optional<Conta> findById(Long id);
    List<Conta> findByClienteId(Long clienteId);
    List<Conta> findByClienteUsuario(Usuario usuario);
    void validateVinculosConta(Long id);
    Conta update(Conta conta);
    void delete(Long id);
}
